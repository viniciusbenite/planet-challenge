package com.planet.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(BookNotAvailableException.class)
  public ResponseEntity<ErrorResponse> handleBookNotAvailable(BookNotAvailableException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("BOOK_NOT_AVAILABLE", ex.getMessage()));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(FailedReservationException.class)
  public ResponseEntity<ErrorResponse> handleFailedReservationException(FailedReservationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("RESERVATION_ERROR", ex.getMessage()));
  }

  @ExceptionHandler(NumberOfReservationsExceededException.class)
  public ResponseEntity<ErrorResponse> handleNumberOfReservationsExceed(NumberOfReservationsExceededException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("NUMBER_OF_RESERVATIONS_EXCEEDED", ex.getMessage()));
  }

  @ExceptionHandler(ReservationAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleReservationExists(ReservationAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("RESERVATION_EXISTS", ex.getMessage()));
  }

  public record ErrorResponse(String error, String message) {}
}
