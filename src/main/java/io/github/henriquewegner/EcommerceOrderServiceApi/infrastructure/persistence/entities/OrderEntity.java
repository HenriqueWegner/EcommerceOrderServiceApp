package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
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

    @Embedded
    private ShippingAddressValueObject shippingAddress;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ShippingEntity shipping;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void linkChildren() {
        if (items != null) {
            items.forEach(i -> i.setOrder(this));
        }
        if (payment != null) {
            payment.setOrder(this);
        }
        if (shipping != null) {
            shipping.setOrder(this);
        }
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + '\'' +
                '}';
    }


}
