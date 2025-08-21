package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItem{

    private UUID id;
    private OrderEntity order;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
}