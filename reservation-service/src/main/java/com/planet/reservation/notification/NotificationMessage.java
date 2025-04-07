package com.planet.reservation.notification;

import com.planet.reservation.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {
  private String email;
  private String userName;
  private String bookTitle;
  private String isbn;
  private ReservationStatus reservationStatus;
}