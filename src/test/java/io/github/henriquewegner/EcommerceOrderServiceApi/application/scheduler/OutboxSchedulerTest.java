package io.github.henriquewegner.EcommerceOrderServiceApi.application.scheduler;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher.EventPublisher;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class OutboxSchedulerTest {

    private OutboxRepository outboxRepository;
    private EventPublisher publisher;
    private OutboxScheduler scheduler;

    @BeforeEach
    void setUp() {
        outboxRepository = mock(OutboxRepository.class);
        publisher = mock(EventPublisher.class);
        scheduler = new OutboxScheduler(outboxRepository, publisher);
    }

    @Test
    void publishOutboxEvents_shouldPublishOrderCreatedAndPaymentEvents() {
        OutboxEventEntity orderCreatedEvent = mock(OutboxEventEntity.class);
        OutboxEventEntity paymentEvent = mock(OutboxEventEntity.class);

        String orderCreatedId = UUID.randomUUID().toString();
        String paymentEventId = UUID.randomUUID().toString();

        when(orderCreatedEvent.getEventType()).thenReturn(EventType.ORDER_EVENT);
        when(orderCreatedEvent.getPayload()).thenReturn("order-payload");
        when(orderCreatedEvent.getAggregateId()).thenReturn(orderCreatedId);

        when(paymentEvent.getEventType()).thenReturn(EventType.PAYMENT_EVENT);
        when(paymentEvent.getPayload()).thenReturn("payment-payload");
        when(paymentEvent.getAggregateId()).thenReturn(paymentEventId);

        List<OutboxEventEntity> events = Arrays.asList(orderCreatedEvent, paymentEvent);
        when(outboxRepository.findByProcessed(Boolean.FALSE)).thenReturn(events);

        scheduler.publishOutboxEvents();

        verify(publisher).publishMessage(
                eq("order-payload"),
                eq(orderCreatedId),
                eq(EventType.ORDER_EVENT),
                eq(orderCreatedEvent)
        );
        verify(publisher).publishMessage(
                eq("payment-payload"),
                eq(paymentEventId),
                eq(EventType.PAYMENT_EVENT),
                eq(paymentEvent)
        );
    }

    @Test
    void publishOutboxEvents_shouldHandleEmptyList() {
        when(outboxRepository.findByProcessed(Boolean.FALSE)).thenReturn(List.of());

        scheduler.publishOutboxEvents();

        verifyNoInteractions(publisher);
    }
}