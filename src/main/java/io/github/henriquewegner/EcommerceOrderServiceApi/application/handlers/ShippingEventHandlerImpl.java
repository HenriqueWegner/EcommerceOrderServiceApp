package io.github.henriquewegner.EcommerceOrderServiceApi.application.handlers;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.OutboxEventService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.StockReservationService;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.factories.OutboxEventEntityFactory;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.ShippingEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.StatusException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShippingEventHandlerImpl implements ShippingEventHandler {

    private final String STATUS_EXCEPTION_MESSAGE = "This status can not be modified by this status.";
    private static final List<OrderStatus> UNAVAILABLE_STATUSES = List.of(
            OrderStatus.CREATED,
            OrderStatus.CANCELLED,
            OrderStatus.DELIVERED,
            OrderStatus.FAILED_PAYMENT
    );

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OutboxEventService outboxEventService;
    private final StockReservationService stockReservationService;


    @Override
    @Transactional
    public Optional<OrderEntity> handleShippingEvent(UUID id, OrderStatus status) {
        log.info("Handling shipping update for order: {}", id);
        return orderRepository.findById(id)
                .map(orderEntity -> {
                    Order order = orderMapper.toDomain(orderEntity);
                    validateStatusTransition(order.getStatus(), status);
                    order.setStatus(status);
                    removeProductFromStock(order);
                    OrderEntity savedOrderEntity = orderRepository.save(order);
                    saveOutboxEvent(savedOrderEntity);

                    return savedOrderEntity;
                });
    }


    private void validateStatusTransition(OrderStatus current, OrderStatus target) {
        if (UNAVAILABLE_STATUSES.contains(current) || current == target) {
            throw new StatusException(STATUS_EXCEPTION_MESSAGE);
        }
        if (current == OrderStatus.PAID && target != OrderStatus.PREPARING_ORDER) {
            throw new StatusException(STATUS_EXCEPTION_MESSAGE);
        }
        if (current == OrderStatus.PREPARING_ORDER && target != OrderStatus.SHIPPED) {
            throw new StatusException(STATUS_EXCEPTION_MESSAGE);
        }
        if (current == OrderStatus.SHIPPED && target != OrderStatus.DELIVERED) {
            throw new StatusException(STATUS_EXCEPTION_MESSAGE);
        }
    }

    private void removeProductFromStock(Order order) {
        try {
            if (order.getStatus().equals(OrderStatus.SHIPPED)) {
                stockReservationService.removeFromStock(order);
            }
        }catch(ExternalApiException e){
            log.error("Stock could not be reserved.");
        }
    }

    private void saveOutboxEvent(OrderEntity orderEntity) {
        outboxEventService.saveOutboxEvent(
                orderEntity, EventType.ORDER_EVENT, orderMapper.toEvent(orderEntity));
    }
}
