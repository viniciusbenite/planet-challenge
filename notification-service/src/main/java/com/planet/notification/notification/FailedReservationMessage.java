package com.planet.notification.notification;

import com.planet.notification.listener.NotificationMessage;
import com.planet.notification.listener.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class FailedReservationMessage implements MessageStrategy {

  @Override
  public ReservationStatus status() {
    return ReservationStatus.FAILED;
  }

  @Override
  public String message(NotificationMessage notification) {
    return String.format(
        "Hi %s, error processing your reservation for \"%s\". Blame the engineers",
        notification.getUserName(), notification.getBookTitle());
  }
}
