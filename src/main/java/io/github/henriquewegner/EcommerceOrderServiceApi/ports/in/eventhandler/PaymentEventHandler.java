package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;

import java.util.Optional;
import java.util.UUID;

public interface PaymentEventHandler {
    Optional<OrderEntity> handlePaymentUpdate(UUID id, PaymentStatus status, String cardToken);
}
