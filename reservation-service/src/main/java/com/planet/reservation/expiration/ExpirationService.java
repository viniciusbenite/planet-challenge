package com.planet.reservation.expiration;

import com.planet.reservation.book.BookService;
import com.planet.reservation.book.BookAction;
import com.planet.reservation.reservation.Reservation;
import com.planet.reservation.reservation.ReservationRepository;
import com.planet.reservation.reservation.ReservationStatus;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpirationService {

  private final BookService bookService;
  private final ReservationRepository reservationRepository;

  @Value("${expiration.scheduler.days.to.expire}")
  private String expirationTimer;

  /** Runs at 1:00AM to expire pending reservations after 7 days */
  @Scheduled(cron = "${expiration.scheduler.period}")
  public List<Reservation> expireReservations() {
    Long sevenDays = Instant.now().minusSeconds(Long.parseLong(expirationTimer)).toEpochMilli();

    List<Reservation> expiredReservations =
        reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, sevenDays);

    return expiredReservations.stream().map(this::expireReservation).collect(Collectors.toList());
  }

  private Reservation expireReservation(Reservation reservation) {
    reservation.setStatus(ReservationStatus.EXPIRED);
    bookService.updateBookInventory(reservation.getBook(), BookAction.ADD);

    return reservationRepository.save(reservation);

    // Notify user
  }
}
