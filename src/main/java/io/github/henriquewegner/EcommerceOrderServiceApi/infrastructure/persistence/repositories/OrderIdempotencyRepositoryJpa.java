package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderIdempotencyRepositoryJpa extends JpaRepository<OrderIdempotencyEntity, String> {

    Optional<OrderIdempotencyEntity> findByIdCustomerIdAndIdIdempotencyKey(UUID customerId, String idempotencyKey);
}
