package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
public class OrderItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price",precision = 18, scale = 2)
    private BigDecimal unitPrice;
}
