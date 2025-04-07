package com.planet.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.notification.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {
  private final ObjectMapper objectMapper;
  private final NotificationService notificationService;

  @RabbitListener(queues = "notification-service-queue")
  public void receive(String message) {
    log.info("Raw message: {}", message);

    NotificationMessage notification;
    try {
      notification = objectMapper.readValue(message, NotificationMessage.class);
    } catch (JsonProcessingException e) {
      log.error("Error serializing message: {}", e.getMessage());
      throw new RuntimeException(e);
    }

    notificationService.notify(notification);
  }
}
