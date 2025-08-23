package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;

public interface PaymentEventHandler {
    void handlePaymentUpdate(PaymentEvent event);
}
