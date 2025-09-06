package io.github.henriquewegner.EcommerceOrderServiceApi.application.scheduler;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher.EventPublisher;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final EventPublisher publisher;

    @Scheduled(fixedDelay = 20000)
    public void publishOutboxEvents() {
        log.info("Checking outbox events to be processed");

        List<OutboxEventEntity> events = outboxRepository.findByProcessed(Boolean.FALSE);

        for (OutboxEventEntity outboxEvent : events) {
            if (outboxEvent.getEventType().equals(EventType.ORDER_EVENT)) {
                publisher.publishMessage(outboxEvent.getPayload(),
                        outboxEvent.getAggregateId(),
                        EventType.ORDER_EVENT,
                        outboxEvent);
            }
            if (outboxEvent.getEventType().equals(EventType.PAYMENT_EVENT)) {
                publisher.publishMessage(outboxEvent.getPayload(),
                        outboxEvent.getAggregateId(),
                        EventType.PAYMENT_EVENT,
                        outboxEvent);
            }

        }
    }


}