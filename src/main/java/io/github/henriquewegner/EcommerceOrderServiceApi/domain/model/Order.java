package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Order {

    private UUID id;
    private UUID customerId;
    private List<OrderItem> items;
    private Currency currency;
    private Payment payment;
    private OrderStatus status;
    private ShippingAddress shippingAddress;
    private Shipping shipping;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

