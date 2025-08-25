package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "outbox_events")
public class OutboxEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Lob
    @Column(name = "payload")
    private String payload;

    @Column(name = "processed")
    private Boolean processed = false;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}

