package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandlerImpl implements PaymentEventHandler {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Optional<OrderEntity> handlePaymentUpdate(UUID id, PaymentStatus status, String cardToken) {
        log.info("Handling payment update for payment: {}", id);

       return orderRepository.findById(id)
                .map(orderEntity -> {
                    Order order = orderMapper.toDomain(orderEntity);
                    PaymentStatus actualStatus = order.getPayment().getPaymentStatus();

                    if (actualStatus == PaymentStatus.SUCCESS || actualStatus == PaymentStatus.FAILED) {
                        log.info("Payment already processed with status: {}", actualStatus);
                        return orderEntity;
                    }

                    preparePayment(status, cardToken, order);
                    prepareOrderStatus(status, order);
                    return orderRepository.save(order);
                });
    }

    private Order preparePayment(PaymentStatus status, String cardToken, Order order) {
        order.getPayment().setPaymentStatus(status);
        order.getPayment().setCardToken(cardToken);

        return order;
    }

    private Order prepareOrderStatus(PaymentStatus status, Order order) {
        if(status.equals(PaymentStatus.SUCCESS)){
            order.setStatus(OrderStatus.PAID);
        }
        if(status.equals(PaymentStatus.FAILED)){
            order.setStatus(OrderStatus.FAILED_PAYMENT);
        }
        return order;
    }

}
