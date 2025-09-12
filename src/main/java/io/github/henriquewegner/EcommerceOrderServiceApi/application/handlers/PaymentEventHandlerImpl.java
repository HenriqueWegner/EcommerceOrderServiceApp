package io.github.henriquewegner.EcommerceOrderServiceApi.application.handlers;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.OutboxEventService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.StockReservationService;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.StatusException;
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
    private final OutboxEventService outboxEventService;
    private final StockReservationService stockReservationService;

    @Override
    @Transactional
    public Optional<OrderEntity> handlePaymentUpdate(UUID id, PaymentStatus status, String cardToken) {
        log.info("Handling payment update for order: {}", id);

       return orderRepository.findById(id)
                .map(orderEntity -> {
                    Order order = orderMapper.toDomain(orderEntity);
                    verifyIfProcessed(order);
                    preparePayment(status, cardToken, order);
                    prepareOrderStatus(status, order);
                    restockProduct(order);
                    OrderEntity savedOrderEntity = orderRepository.save(order);
                    saveOutboxEvent(savedOrderEntity);

                    return savedOrderEntity;
                });
    }

    private void verifyIfProcessed(Order order) {
        PaymentStatus actualStatus = order.getPayment().getPaymentStatus();
        if (actualStatus == PaymentStatus.SUCCESS || actualStatus == PaymentStatus.FAILED) {
            log.info("Payment already processed with status: {}", actualStatus);
            throw new StatusException("Payment has already been processed.");
        }
    }

    private void preparePayment(PaymentStatus status, String cardToken, Order order) {
        order.getPayment().setPaymentStatus(status);
        order.getPayment().setCardToken(cardToken);
    }

    private void prepareOrderStatus(PaymentStatus status, Order order) {
        if(status.equals(PaymentStatus.SUCCESS)){
            order.setStatus(OrderStatus.PAID);
        }
        if(status.equals(PaymentStatus.FAILED)){
            order.setStatus(OrderStatus.FAILED_PAYMENT);
        }
    }

    private void saveOutboxEvent(OrderEntity orderEntity) {
        outboxEventService.saveOutboxEvent(
                orderEntity,EventType.ORDER_EVENT,orderMapper.toEvent(orderEntity));
    }

    private void restockProduct(Order order) {
        if(order.getStatus().equals(OrderStatus.FAILED_PAYMENT)){
            try{
                stockReservationService.restockProduct(order);
            }catch(ExternalApiException e){
                log.error("Stock could not be reserved.");
            }
        }
    }
}
