package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.kafka.consumer;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final PaymentEventHandler handler;

    @KafkaListener(topics = "${spring.kafka.topic.payment-update}", groupId = "my-group-id")
    public void consume(PaymentEvent event) {
        handler.handlePaymentUpdate(event);
    }

}
