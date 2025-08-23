package io.github.henriquewegner.EcommerceOrderServiceApi.domain.event;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreatedEvent {
    private String id;
    private CustomerCreatedEvent customer;
    private List<OrderItemCreatedEvent> items;
    private OrderStatus status;
}
