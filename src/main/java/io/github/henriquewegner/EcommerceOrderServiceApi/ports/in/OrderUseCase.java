package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;

public interface OrderUseCase {

    void createOrder(OrderRequestDTO orderDTO);
}
