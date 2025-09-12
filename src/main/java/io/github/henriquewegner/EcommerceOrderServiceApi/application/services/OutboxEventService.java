package io.github.henriquewegner.EcommerceOrderServiceApi.application.services;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.factories.OutboxEventEntityFactory;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxRepository outboxRepository;

    public void saveOutboxEvent(OrderEntity orderEntity, EventType eventType, Object payload) {
        OutboxEventEntity event = OutboxEventEntityFactory.create(
                orderEntity.getId().toString(),
                eventType,
                payload);
        outboxRepository.save(event);
    }
}