package com.planet.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationAlreadyExistsException extends RuntimeException {
    public ReservationAlreadyExistsException(Long bookId, Long userId) {
        super("Reservation already exists for book and user: %s:%s".formatted(bookId, userId));
    }
}
