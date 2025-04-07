package com.planet.notification.notification;

import com.planet.notification.listener.ReservationStatus;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MessageStrategyFactory {

  private final Map<ReservationStatus, MessageStrategy> strategies;

  public MessageStrategyFactory(List<MessageStrategy> strategies) {
    this.strategies = new EnumMap<>(ReservationStatus.class);

    registerStrategies(strategies);
  }

  public MessageStrategy getStrategy(ReservationStatus status) {
    return Optional.ofNullable(strategies.get(status))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Could not associate any strategy for status " + status));
  }

  private void registerStrategies(List<MessageStrategy> strategies) {
    strategies.forEach(strategy -> this.strategies.put(strategy.status(), strategy));
  }
}
