package com.planet.notification.notification;

import com.planet.notification.listener.NotificationMessage;
import com.planet.notification.listener.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class CreatedReservationMessage implements MessageStrategy {

  @Override
  public ReservationStatus status() {
    return ReservationStatus.PENDING;
  }

  @Override
  public String message(NotificationMessage notification) {
    return String.format("Hi %s, your reservation for \"%s\" is confirmed",
        notification.getUserName(), notification.getBookTitle());
  }
}
