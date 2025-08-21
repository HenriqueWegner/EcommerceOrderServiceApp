package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "amount",precision = 18, scale = 2)
    private BigDecimal amount;
}
