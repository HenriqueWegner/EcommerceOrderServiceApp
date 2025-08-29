package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.factories;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutboxEventEntityFactoryTest {

    @Test
    void create_setsAllFields() {
        String aggregateId = "123";
        EventType eventType = EventType.ORDER_CREATED;
        Object payload = "payload";

        OutboxEventEntity entity = OutboxEventEntityFactory.create(aggregateId, eventType, payload);

        assertEquals(aggregateId, entity.getAggregateId());
        assertEquals(eventType, entity.getEventType());
        assertEquals(payload, entity.getPayload());
    }
}