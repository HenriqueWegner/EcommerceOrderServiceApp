package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "shippings")
@Data
@ToString(exclude = "order")
public class ShippingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "cep_origin")
    private String cepOrigin;

    @Column(name = "cep_destination")
    private String cepDestination;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
