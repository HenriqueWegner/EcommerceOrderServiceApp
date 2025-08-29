package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders_idempotency")
@EntityListeners(AuditingEntityListener.class)
public class OrderIdempotencyEntity {

    @EmbeddedId
    OrderIdempotencyId id;

    @Column(name = "request_hash")
    String requestHash;

    @Column(name = "response", columnDefinition = "jsonb")
    @Type(JsonType.class)
    String response;

    @Column(name = "created_at")
    @CreatedDate
    LocalDateTime createdAt;
}
