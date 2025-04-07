package com.planet.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedReservationException extends RuntimeException {
  public FailedReservationException(Long bookId, Long userId) {
    super("Reservation failed for book {%s} and user {%s}".formatted(bookId, userId));
  }
}
