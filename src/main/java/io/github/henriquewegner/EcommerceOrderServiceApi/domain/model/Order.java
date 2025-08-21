package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerId;

    @OneToMany
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @OneToOne
    private Payment payment;

    @Column(name = "card_token")
    private String cardToken;

    @Column(name = "idempotency_key")
    private String idempotencyKey;
}
