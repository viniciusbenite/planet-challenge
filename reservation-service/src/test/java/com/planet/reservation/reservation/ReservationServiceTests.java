package com.planet.reservation.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.planet.reservation.book.Book;
import com.planet.reservation.book.BookAction;
import com.planet.reservation.book.BookService;
import com.planet.reservation.exception.BookNotAvailableException;
import com.planet.reservation.exception.EntityNotFoundException;
import com.planet.reservation.exception.NumberOfReservationsExceededException;
import com.planet.reservation.exception.ReservationAlreadyExistsException;
import com.planet.reservation.notification.NotificationMessage;
import com.planet.reservation.notification.NotificationProducer;
import com.planet.reservation.user.User;
import com.planet.reservation.user.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {

  @Mock ReservationRepository reservationRepository;

  @Mock ReservationMapper reservationMapper;

  @Mock UserService userService;

  @Mock BookService bookService;

  @Mock NotificationProducer notificationProducer;

  @InjectMocks ReservationService reservationService;

  Book book;
  User user;
  Reservation reservation;
  ReservationDTO reservationDTO;

  @BeforeEach
  void setUp() {
    book =
        Book.builder()
            .title("The Catcher in the Rye")
            .author("Salinger, J. D.")
            .isbn("isbn")
            .copies(5)
            .build();
    user =
        User.builder().name("Vinicius").email("vinicius@email.com").reservations(List.of()).build();

    long now = Instant.now().toEpochMilli();

    reservation =
        Reservation.builder()
            .user(user)
            .book(book)
            .status(ReservationStatus.PENDING)
            .createdAt(now)
            .build();

    reservationDTO =
        ReservationDTO.builder()
            .user(user)
            .book(book)
            .status(ReservationStatus.PENDING)
            .createdAt(now)
            .build();
  }

  @Test
  void when_reserveBook_success_shouldReturnDTO() {
    Long bookId = 1L;
    Long userId = 1L;

    when(bookService.findBook(bookId)).thenReturn(book);
    when(userService.findUser(userId)).thenReturn(user);
    when(bookService.isBookAvailable(bookId)).thenReturn(true);
    when(reservationRepository.existsByUserIdAndBookId(userId, bookId)).thenReturn(false);
    when(reservationRepository.countByUserIdAndStatusIsNot(userId, ReservationStatus.EXPIRED))
        .thenReturn(2L);
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
    when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

    ReservationDTO expected = reservationService.reserveBook(bookId, userId);

    Assertions.assertEquals(expected, reservationDTO);
    verify(notificationProducer).send(any(NotificationMessage.class));
    verify(bookService).updateBookInventory(book, BookAction.REMOVE);
  }

  @Test
  void when_reserveBook_noAvailability_shouldThrowException() {
    Long bookId = 1L;
    Long userId = 1L;

    when(bookService.findBook(bookId)).thenReturn(book);
    when(userService.findUser(userId)).thenReturn(user);
    when(bookService.isBookAvailable(bookId)).thenReturn(false);

    assertThrows(
        BookNotAvailableException.class, () -> reservationService.reserveBook(bookId, userId));
  }

  @Test
  void when_reserveBook_reservationExists_shouldThrowException() {
    long bookId = 1L;
    long userId = 1L;

    when(bookService.findBook(bookId)).thenReturn(book);
    when(userService.findUser(userId)).thenReturn(user);
    when(bookService.isBookAvailable(bookId)).thenReturn(true);
    when(reservationRepository.existsByUserIdAndBookId(userId, bookId)).thenReturn(true);

    assertThrows(
        ReservationAlreadyExistsException.class,
        () -> reservationService.reserveBook(bookId, userId));
  }

  @Test
  void when_reserveBook_tooManyReservations_shouldThrowException() {
    long bookId = 1L;
    long userId = 1L;

    when(bookService.findBook(bookId)).thenReturn(book);
    when(userService.findUser(userId)).thenReturn(user);
    when(bookService.isBookAvailable(bookId)).thenReturn(true);
    when(reservationRepository.existsByUserIdAndBookId(userId, bookId)).thenReturn(false);
    when(reservationRepository.countByUserIdAndStatusIsNot(userId, ReservationStatus.EXPIRED))
        .thenReturn(3L);

    assertThrows(
        NumberOfReservationsExceededException.class,
        () -> reservationService.reserveBook(bookId, userId));
  }

  @Test
  void when_pickUpReservation_success_shouldUpdateStatus() {
    long reservationId = 1L;
    reservation.setStatus(ReservationStatus.PENDING);

    when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
    when(reservationRepository.save(reservation)).thenReturn(reservation);
    when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

    ReservationDTO expected = reservationService.pickUpReservation(1L);

    assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
    assertEquals(reservationDTO, expected);
  }

  @Test
  void when_removeReservation_success() {
    long reservationId = 1L;

    when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

    reservationService.removeReservation(reservationId);

    verify(bookService).updateBookInventory(book, BookAction.ADD);
    verify(reservationRepository).delete(reservation);
  }

  @Test
  void when_getReservation_dontExists_shouldThrowException() {
    long reservationId = 1L;

    when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> reservationService.getReservation(reservationId));
  }
}
