package com.planet.notification.notification;

import com.planet.notification.listener.NotificationMessage;
import com.planet.notification.listener.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final MessageStrategyFactory factory;

  public void notify(NotificationMessage notification) {
    ReservationStatus status = notification.getReservationStatus();
    MessageStrategy strategy = factory.getStrategy(status);

    String messageToBeSent = strategy.message(notification);

    log.info(
        "Notification service sent to: {} - with status: {} - with message body: {}",
        notification.getEmail(),
        status,
        messageToBeSent);
  }
}
