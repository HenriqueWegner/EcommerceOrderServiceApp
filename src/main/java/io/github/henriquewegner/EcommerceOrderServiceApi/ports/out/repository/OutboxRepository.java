package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;

import java.util.List;

public interface OutboxRepository {

    OutboxEventEntity save(OutboxEventEntity outboxEvent);

    List<OutboxEventEntity> findByProcessed(boolean processed);
}