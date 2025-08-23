package io.github.henriquewegner.EcommerceOrderServiceApi.domain.event;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemCreatedEvent {

    private String sku;
    private Integer quantity;
    private BigDecimal amount;
}
