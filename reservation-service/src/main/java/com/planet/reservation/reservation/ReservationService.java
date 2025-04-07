package com.planet.reservation.reservation;

import com.planet.reservation.book.Book;
import com.planet.reservation.book.BookService;
import com.planet.reservation.book.BookAction;
import com.planet.reservation.exception.BookNotAvailableException;
import com.planet.reservation.exception.EntityNotFoundException;
import com.planet.reservation.exception.FailedReservationException;
import com.planet.reservation.exception.NumberOfReservationsExceededException;
import com.planet.reservation.exception.ReservationAlreadyExistsException;
import com.planet.reservation.notification.NotificationMessage;
import com.planet.reservation.notification.NotificationProducer;
import com.planet.reservation.user.User;
import com.planet.reservation.user.UserService;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final BookService bookService;
  private final UserService userService;
  private final NotificationProducer notificationProducer;

  @Transactional
  public ReservationDTO reserveBook(Long bookId, Long userId) {
    Book book = bookService.findBook(bookId);
    User user = userService.findUser(userId);

    validateBookAvailability(bookId);
    validateIfReservationExist(userId, bookId);
    validateTotalReservationByUser(userId);

    long now = Instant.now().toEpochMilli();

    Reservation r =
        Reservation.builder()
            .book(book)
            .user(user)
            .status(ReservationStatus.PENDING)
            .createdAt(now)
            .build();

    try {
      ReservationDTO reservation = reservationMapper.toDTO(reservationRepository.save(r));
      bookService.updateBookInventory(book, BookAction.REMOVE);

      buildAndSendNotification(book, user, ReservationStatus.PENDING);

      log.info("Reservation has been successful: {}", reservation.getReservationId());

      return reservation;
    } catch (Exception e) {
      log.error("Error reserving book: {}", e.getMessage());
      buildAndSendNotification(book, user, ReservationStatus.FAILED);

      throw new FailedReservationException(bookId, userId);
    }
  }

  @Cacheable(value = "reservations", key = "#reservationId")
  public ReservationDTO getReservation(Long reservationId) {
    return reservationMapper.toDTO(findReservation(reservationId));
  }

  @CacheEvict(value = "reservations", key = "#reservationId")
  public ReservationDTO pickUpReservation(Long reservationId) {
    Reservation reservation = findReservation(reservationId);
    reservation.setStatus(ReservationStatus.ACTIVE);

    log.info("Reservation has been successfully pickedUp: {}", reservation);

    return reservationMapper.toDTO(reservationRepository.save(reservation));
  }

  @CacheEvict(value = "reservations", key = "#reservationId")
  public void removeReservation(Long reservationId) {
    Reservation reservation = findReservation(reservationId);
    bookService.updateBookInventory(reservation.getBook(), BookAction.ADD);

    log.info("Reservation has been successfully removed: {}", reservation);

    reservationRepository.delete(reservation);
  }

  private Reservation findReservation(Long reservationId) {
    return reservationRepository
        .findById(reservationId)
        .orElseThrow(
            () -> new EntityNotFoundException("Reservation %s not found".formatted(reservationId)));
  }

  private void validateBookAvailability(Long bookId) {
    boolean isBookAvailable = bookService.isBookAvailable(bookId);
    if (!isBookAvailable) {
      throw new BookNotAvailableException(bookId);
    }
  }

  private void validateTotalReservationByUser(long userId) {
    long numberOfReservations =
        reservationRepository.countByUserIdAndStatusIsNot(userId, ReservationStatus.EXPIRED);
    if (numberOfReservations >= 3) {
      throw new NumberOfReservationsExceededException(userId);
    }
  }

  private void  validateIfReservationExist(long userId, long bookId) {
    boolean reservationExists = reservationRepository.existsByUserIdAndBookId(userId, bookId);
    if (reservationExists) {
      throw new ReservationAlreadyExistsException(bookId, userId);
    }
  }

  private void buildAndSendNotification(Book book, User user, ReservationStatus status) {
    NotificationMessage notificationMessage =
        NotificationMessage.builder()
            .email(user.getEmail())
            .userName(user.getName())
            .bookTitle(book.getTitle())
            .isbn(book.getIsbn())
            .reservationStatus(status)
            .build();

    notificationProducer.send(notificationMessage);
  }
}
