package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandlerUseCase implements PaymentEventHandler {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public void handlePaymentUpdate(PaymentEvent event) {
        log.info("Handling payment update for payment: {}",event.getId());

        orderRepository.findById(event.getOrder().getId())
                .ifPresent(orderEntity -> {
                    Order order = orderMapper.toDomain(orderEntity);
                    PaymentStatus actualStatus = order.getPayment().getPaymentStatus();

                    if (actualStatus == PaymentStatus.SUCCESS || actualStatus == PaymentStatus.FAILED) {
                        log.info("Payment already processed with status: {}", actualStatus);
                        return;
                    }

                    order = preparePayment(event, order);
                    orderRepository.save(order);
                });
    }


    private Order preparePayment(PaymentEvent event, Order order) {
        order.getPayment().setPaymentStatus(event.getPaymentStatus());
        order.getPayment().setCardToken(event.getCardToken());

        return order;
    }


}
