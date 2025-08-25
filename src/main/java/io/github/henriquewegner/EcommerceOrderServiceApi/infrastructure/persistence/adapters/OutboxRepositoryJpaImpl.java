package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OutboxRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryJpaImpl implements OutboxRepository {

    private final OutboxRepositoryJpa outboxRepositoryJpa;

    @Override
    public OutboxEventEntity save(OutboxEventEntity outboxEventEntity) {
        return outboxRepositoryJpa.save(outboxEventEntity);
    }

    @Override
    public List<OutboxEventEntity> findByProcessed(boolean processed) {
        return outboxRepositoryJpa.findByProcessed(processed);
    }
}
