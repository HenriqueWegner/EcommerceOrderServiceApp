package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentEntity payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @PrePersist
    @PreUpdate
    private void linkChildren() {
        if (items != null) {
            items.forEach(i -> i.setOrder(this));
        }
        if (payment != null) {
            payment.setOrder(this);
        }
    }

}
