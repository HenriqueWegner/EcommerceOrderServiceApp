package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.kafka.producer;


import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KafkaEventPublisherTest {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private OutboxRepository outboxRepository;
    private KafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        outboxRepository = mock(OutboxRepository.class);
        publisher = new KafkaEventPublisher(kafkaTemplate, outboxRepository);

        ReflectionTestUtils.setField(publisher, "ORDER_CREATED", "order-topic");
        ReflectionTestUtils.setField(publisher, "PAYMENT_EVENT", "payment-topic");
    }

    @Test
    void publishMessage_shouldSendToOrderTopicAndMarkOutboxProcessed() {
        Object event = new Object();
        String key = "order-key";
        OutboxEventEntity outboxEvent = new OutboxEventEntity();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();

        SendResult<String, Object> sendResult = mock(SendResult.class);
        RecordMetadata metadata = mock(RecordMetadata.class);
        when(metadata.topic()).thenReturn("order-topic");
        when(metadata.offset()).thenReturn(123L);
        when(sendResult.getRecordMetadata()).thenReturn(metadata);

        when(kafkaTemplate.send(eq("order-topic"), eq(key), eq(event))).thenReturn(future);
        when(outboxRepository.save(any())).thenReturn(outboxEvent);

        publisher.publishMessage(event, key, EventType.ORDER_EVENT, outboxEvent);

        // Simulate successful send
        future.complete(sendResult);

        // Outbox should be marked as processed and saved
        assertTrue(outboxEvent.getProcessed());
        verify(outboxRepository, times(1)).save(outboxEvent);
    }

    @Test
    void publishMessage_shouldSendToPaymentTopic() {
        Object event = new Object();
        String key = "payment-key";
        OutboxEventEntity outboxEvent = new OutboxEventEntity();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();

        SendResult<String, Object> sendResult = mock(SendResult.class);
        RecordMetadata metadata = mock(RecordMetadata.class);
        when(metadata.topic()).thenReturn("payment-topic");
        when(metadata.offset()).thenReturn(456L);
        when(sendResult.getRecordMetadata()).thenReturn(metadata);

        when(kafkaTemplate.send(eq("payment-topic"), eq(key), eq(event))).thenReturn(future);
        when(outboxRepository.save(any())).thenReturn(outboxEvent);

        publisher.publishMessage(event, key, EventType.PAYMENT_EVENT, outboxEvent);

        // Simulate successful send
        future.complete(sendResult);

        assertTrue(outboxEvent.getProcessed());
        verify(outboxRepository, times(1)).save(outboxEvent);
    }

    @Test
    void publishMessage_shouldNotMarkOutboxOnFailure() {
        Object event = new Object();
        String key = "order-key";
        OutboxEventEntity outboxEvent = new OutboxEventEntity();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();

        when(kafkaTemplate.send(eq("order-topic"), eq(key), eq(event))).thenReturn(future);

        publisher.publishMessage(event, key, EventType.ORDER_EVENT, outboxEvent);

        // Simulate failure
        future.completeExceptionally(new RuntimeException("Kafka error"));

        assertFalse(outboxEvent.getProcessed());
        verify(outboxRepository, never()).save(outboxEvent);
    }

    @Test
    void selectTopic_shouldThrowOnUnknownEventType() {
        Object event = new Object();
        String key = "unknown-key";
        OutboxEventEntity outboxEvent = new OutboxEventEntity();

        assertThrows(IllegalArgumentException.class, () ->
                publisher.publishMessage(event, key, null, outboxEvent));
    }
}