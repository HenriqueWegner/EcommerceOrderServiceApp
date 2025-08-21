package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public void createOrder(OrderEntity order) {

        order.getPayment().setPaymentStatus(PaymentStatus.NOT_PAID);
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

    }
}
