package com.planet.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NumberOfReservationsExceededException extends RuntimeException {
    public NumberOfReservationsExceededException(Long id) {
        super("Number of reservations exceeded for user: %s".formatted(id));
    }
}
