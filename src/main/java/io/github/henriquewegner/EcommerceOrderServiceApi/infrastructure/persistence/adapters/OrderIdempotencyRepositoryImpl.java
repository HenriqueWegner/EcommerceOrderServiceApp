package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OrderIdempotencyRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderIdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderIdempotencyRepositoryImpl implements OrderIdempotencyRepository {

    private final OrderIdempotencyRepositoryJpa orderIdempotencyRepositoryJpa;

    @Override
    public Optional<OrderIdempotencyEntity> findByIdCustomerIdAndIdIdempotencyKey(UUID customerId, String idempotencyKey) {
        return orderIdempotencyRepositoryJpa.findByIdCustomerIdAndIdIdempotencyKey(customerId,idempotencyKey);
    }

    @Override
    public OrderIdempotencyEntity save(OrderIdempotencyEntity entity) {
        return orderIdempotencyRepositoryJpa.save(entity);
    }
}
