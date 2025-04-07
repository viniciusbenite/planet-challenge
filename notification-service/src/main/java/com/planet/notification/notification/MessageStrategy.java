package com.planet.notification.notification;

import com.planet.notification.listener.NotificationMessage;
import com.planet.notification.listener.ReservationStatus;

public interface MessageStrategy {
  ReservationStatus status();

  String message(NotificationMessage notification);
}
