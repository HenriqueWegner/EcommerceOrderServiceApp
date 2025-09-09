package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.factories.OutboxEventEntityFactory;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.ShippingEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
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
    private final OutboxRepository outboxRepository;
    private final OrderMapper orderMapper;


    @Override
    @Transactional
    public Optional<OrderEntity> handleShippingEvent(UUID id, OrderStatus status) {
        log.info("Handling shipping update for order: {}", id);
        return orderRepository.findById(id)
                .map(orderEntity -> {
                    Order order = orderMapper.toDomain(orderEntity);
                    validateStatusTransition(order.getStatus(), status);
                    order.setStatus(status);
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


    private void saveOutboxEvent(OrderEntity orderEntity) {
        OutboxEventEntity orderCreatedEvent = OutboxEventEntityFactory.create(
                orderEntity.getId().toString(),
                EventType.ORDER_EVENT,
                orderMapper.toEvent(orderEntity));

        outboxRepository.save(orderCreatedEvent);
    }
}
