package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "card_token")
    private String cardToken;

    @Column(name = "amount",precision = 18, scale = 2)
    private BigDecimal amount;
}
