package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;

import java.util.Optional;
import java.util.UUID;

public interface ShippingEventHandler {
    Optional<OrderEntity> handleShippingEvent(UUID id, OrderStatus status);
}
