package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.kafka.producer;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.OrderCreatedEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    @Value("${spring.kafka.topic.order-event}")
    private String ORDER_EVENT_TOPIC;
    @Value("${spring.kafka.topic.payment-event}")
    private String PAYMENT_EVENT_TOPIC;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(ORDER_EVENT_TOPIC, event.getId(), event);
    }

    @Override
    public void publishPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send(PAYMENT_EVENT_TOPIC, event);
    }
}
