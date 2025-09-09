package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository{
    List<OrderEntity> findByCustomerId(UUID customerId);

    OrderEntity save(Order order);

    Optional<OrderEntity> findById(UUID id);

}
