package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.kafka.producer;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.OrderCreatedEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher.EventPublisher;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    @Value("${spring.kafka.topic.order-event}")
    private String ORDER_CREATED;
    @Value("${spring.kafka.topic.payment-event}")
    private String PAYMENT_EVENT;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    @Override
    public void publishMessage(Object event, String key, EventType eventType, OutboxEventEntity outboxEvent) {

        String topic = selectTopic(eventType);

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) ->
        {
            if (ex == null) {
                log.info("Message published with success to the topic {} with offset {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset());

                outboxEvent.setProcessed(Boolean.TRUE);
                outboxRepository.save(outboxEvent);
                log.info("Published event: {}", outboxEvent.getId());

            } else {
                log.error("Failed to publish message", ex);
            }
        });
    }

    private String selectTopic(EventType eventType) {

        if (eventType == null) {
            throw new IllegalArgumentException("EventType must not be null");
        }

        String topic;
        switch(eventType){
            case ORDER_CREATED : topic = ORDER_CREATED;
                break;
            case PAYMENT_EVENT : topic = PAYMENT_EVENT;
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);

        }
        return topic;
    }

}

