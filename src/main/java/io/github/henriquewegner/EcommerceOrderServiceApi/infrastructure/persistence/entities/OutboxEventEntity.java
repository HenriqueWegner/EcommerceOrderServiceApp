package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "outbox_events")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "payload", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private Object payload;

    @Column(name = "processed")
    private Boolean processed = false;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
