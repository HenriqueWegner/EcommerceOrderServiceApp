package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.factories;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;

public class OutboxEventEntityFactory {

    public static OutboxEventEntity create(String aggregateId, EventType eventType,Object payload) {
        OutboxEventEntity event = new OutboxEventEntity();
        event.setAggregateId(aggregateId);
        event.setEventType(eventType);
        event.setPayload(payload);

        return event;
    }
}
