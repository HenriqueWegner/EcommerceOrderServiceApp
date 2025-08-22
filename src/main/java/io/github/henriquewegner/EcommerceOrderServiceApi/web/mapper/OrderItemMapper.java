package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "order", ignore = true)
    OrderItem toDomain(OrderItemEntity entity);

    List<OrderItem> toDomain(List<OrderItemEntity> entities);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toEntity(OrderItem item);

    List<OrderItemEntity> toEntity(List<OrderItem> items);
}