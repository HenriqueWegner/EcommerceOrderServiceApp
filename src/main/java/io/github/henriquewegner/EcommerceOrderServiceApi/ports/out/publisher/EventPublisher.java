package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.OrderCreatedEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;

public interface EventPublisher {
    void publishOrderCreated(String event, String key);
    void publishPaymentEvent(String event, String key);
}
