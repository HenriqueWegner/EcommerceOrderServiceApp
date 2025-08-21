package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;

public interface OrderUseCase {

    void createOrder(OrderEntity order);
}
