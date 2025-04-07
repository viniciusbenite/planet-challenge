package com.planet.reservation.reservation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
  private final ReservationService reservationService;

  @PostMapping(value = "/{bookId}/{userId}")
  public ResponseEntity<ReservationDTO> reserveBook(
      @PathVariable
          @NotNull(message = "Book ID must not be null")
          @Positive(message = "Book ID has to be bigger than 0")
          Long bookId,
      @PathVariable
          @NotNull(message = "User ID must not be null")
          @Positive(message = "User ID has to be bigger than 0")
          Long userId) {

    ReservationDTO reservation = reservationService.reserveBook(bookId, userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
  }

  @GetMapping(value = "/{reservationId}")
  public ResponseEntity<ReservationDTO> getReservation(
      @PathVariable
          @NotNull(message = "Reservation ID must not be null")
          @Positive(message = "Reservation ID has to be bigger than 0")
          Long reservationId) {

    ReservationDTO reservation = reservationService.getReservation(reservationId);

    return ResponseEntity.ok(reservation);
  }

  @DeleteMapping(value = "/{reservationId}")
  public ResponseEntity<Void> cancelReservation(
      @PathVariable
          @NotNull(message = "Reservation ID must not be null")
          @Positive(message = "Reservation ID has to be bigger than 0")
          Long reservationId) {

    reservationService.removeReservation(reservationId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{reservationId}")
  public ResponseEntity<ReservationDTO> pickupReservation(
      @PathVariable
          @NotNull(message = "Reservation ID must not be null")
          @Positive(message = "Reservation ID has to be bigger than 0")
          Long reservationId) {

    ReservationDTO reservation = reservationService.pickUpReservation(reservationId);

    return ResponseEntity.ok().body(reservation);
  }
}
