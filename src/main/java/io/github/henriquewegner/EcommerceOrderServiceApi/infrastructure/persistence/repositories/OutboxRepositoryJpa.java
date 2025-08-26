package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepositoryJpa extends JpaRepository<OutboxEventEntity,Long> {

    List<OutboxEventEntity> findByProcessed(boolean processed);
}