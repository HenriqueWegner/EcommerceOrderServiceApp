package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;

public interface EventPublisher {
    void publishMessage(Object event, String key, EventType eventType, OutboxEventEntity outboxEvent);
}
