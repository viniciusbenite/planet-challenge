package com.planet.reservation.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class RabbitMqConfiguration {
    @Bean
    public Queue notificationServiceQueue() {
        return new Queue("notification-service-queue", true);
    }

    @Bean
    public DirectExchange dataOperationExchange() {
        return new DirectExchange("data-operation-exchange");
    }

    @Bean
    public Binding notificationServiceBinding() {
        return BindingBuilder.bind(notificationServiceQueue())
                .to(dataOperationExchange())
                .with("notification.service.key");
    }
}
