package com.planet.reservation.expiration;

import com.planet.reservation.book.BookDTO;
import com.planet.reservation.book.BookService;
import com.planet.reservation.reservation.Reservation;
import com.planet.reservation.reservation.ReservationRepository;
import com.planet.reservation.reservation.ReservationStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpirationServiceTests {

  @Mock ReservationRepository reservationRepository;
  @Mock BookService bookService;

  @InjectMocks ExpirationService expirationService;

  Reservation reservation1;
  Reservation reservation2;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(expirationService, "expirationTimer", "604800");

    long now = Instant.now().toEpochMilli();

    reservation1 =
        Reservation.builder()
            .id(1L)
            .status(ReservationStatus.PENDING)
            .createdAt(Instant.now().minusSeconds(now).toEpochMilli())
            .build();

    reservation2 =
        Reservation.builder()
            .id(2L)
            .status(ReservationStatus.PENDING)
            .createdAt(Instant.now().minusSeconds(now).toEpochMilli())
            .build();
  }

  @Test
  void when_expiringReservations_shouldReturnExpiredReservations() {
    when(reservationRepository.findByStatusAndCreatedAtBefore(any(), any()))
        .thenReturn(List.of(reservation1, reservation2));
    when(bookService.updateBookInventory(any(), any())).thenReturn(BookDTO.builder().build());

    Reservation updatedRes1 = reservation1;
    updatedRes1.setStatus(ReservationStatus.EXPIRED);
    Reservation updatedRes2 = reservation2;
    updatedRes2.setStatus(ReservationStatus.EXPIRED);

    when(reservationRepository.save(reservation1)).thenReturn(updatedRes1);
    when(reservationRepository.save(reservation2)).thenReturn(updatedRes2);

    List<Reservation> expired = expirationService.expireReservations();

    assertThat(expired.size()).isEqualTo(2);
    assertThat(expired.get(0).getStatus()).isEqualTo(ReservationStatus.EXPIRED);
    assertThat(expired.get(1).getStatus()).isEqualTo(ReservationStatus.EXPIRED);
  }
}
