package com.planet.reservation.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    public static final String NOTIFICATION_SERVICE_KEY = "notification.service.key";
    public static final String DATA_OPERATION_EXCHANGE = "data-operation-exchange";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void send(NotificationMessage notificationMessage) {
        String correlationId = String.valueOf(System.currentTimeMillis());

        try {
            String messageBody = objectMapper.writeValueAsString(notificationMessage);

            Message message =
                MessageBuilder.withBody(messageBody.getBytes(StandardCharsets.UTF_8))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setCorrelationId(correlationId)
                    .build();

            rabbitTemplate.send(DATA_OPERATION_EXCHANGE, NOTIFICATION_SERVICE_KEY, message);

            log.info("Sent notification message to notification-queue. Msg: {}", messageBody);
        } catch (JsonProcessingException e) {
            log.error("Error serializing notification message: {}", e.getMessage());
        }
    }
}
