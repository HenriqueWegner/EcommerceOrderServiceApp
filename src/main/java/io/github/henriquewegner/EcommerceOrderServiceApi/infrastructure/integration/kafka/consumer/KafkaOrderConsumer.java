package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.kafka.consumer;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.ShippingEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.ShippingEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final PaymentEventHandler paymentEventHandler;
    private final ShippingEventHandler shippingEventHandler;

    @KafkaListener(topics = "${spring.kafka.topic.payment-update}",
            groupId = "payment-group",
            containerFactory = "paymentKafkaListenerFactory")
    public void consumePaymentEvent(PaymentEvent event) {
        paymentEventHandler.handlePaymentUpdate(event.getOrder().getId(),event.getPaymentStatus(), event.getCardToken());
    }

    @KafkaListener(topics = "${spring.kafka.topic.shipping-update}",
            groupId = "shipping-group",
            containerFactory = "shippingKafkaListenerFactory")
    public void consumeShippingUpdate(ShippingEvent event) {
        shippingEventHandler.handleShippingEvent(event.getId(), event.getStatus());
    }

}
