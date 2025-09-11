package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.OrderCreatedEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OrderItemMapper.class,
                PaymentMapper.class,
                ShippingMapper.class,
                ShippingAddressMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    Order toDomain(OrderRequestDTO dto);

    Order toDomain(OrderEntity orderEntity);

    @Mapping(target = "items", source = "items")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    OrderEntity toEntity(Order order);

    @Mapping(target = "items", source = "items")
    OrderResponseDTO toDto(OrderEntity entity);

    List<OrderResponseDTO> entityListToDtoList(List<OrderEntity> entityList);

    @Mapping(target = "id", source = "id")
    CreatedOrderResponseDTO orderEntityToCreatedOrderResponseDTO(OrderEntity orderEntity);

    OrderCreatedEvent toEvent(OrderEntity entity);

}