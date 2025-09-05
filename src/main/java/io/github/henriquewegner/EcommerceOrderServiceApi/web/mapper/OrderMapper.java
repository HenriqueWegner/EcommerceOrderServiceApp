package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.OrderCreatedEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CustomerMapper.class,
                OrderItemMapper.class,
                PaymentMapper.class,
                ShippingMapper.class,
                ShippingAddressMapper.class})
public interface OrderMapper {

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "id", ignore = true)
    Order toDomain(OrderRequestDTO dto, Customer customer);

    List<Order> toDomain(List<OrderEntity> entities);

    Order toDomain(OrderEntity orderEntity);

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    OrderEntity toEntity(Order order);

    @Mapping(target = "items", source = "items")
    OrderResponseDTO toDto(OrderEntity entity);

    @Mapping(target = "id", source = "id")
    CreatedOrderResponseDTO orderEntityToCreatedOrderResponseDTO(OrderEntity orderEntity);

    OrderCreatedEvent toEvent(OrderEntity entity);

    List<OrderResponseDTO> entityListToDtoList(List<OrderEntity> entityList);
}