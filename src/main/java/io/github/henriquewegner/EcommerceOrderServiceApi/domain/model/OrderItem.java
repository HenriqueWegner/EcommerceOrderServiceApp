package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItem{

    private UUID id;
    private Order order;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
}