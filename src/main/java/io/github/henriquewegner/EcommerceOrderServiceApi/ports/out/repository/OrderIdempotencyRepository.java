package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;

import java.util.Optional;
import java.util.UUID;

public interface OrderIdempotencyRepository {

    Optional<OrderIdempotencyEntity> findByIdCustomerIdAndIdIdempotencyKey(UUID customerId, String idempotencyKey);

    OrderIdempotencyEntity save(OrderIdempotencyEntity entity);
}
