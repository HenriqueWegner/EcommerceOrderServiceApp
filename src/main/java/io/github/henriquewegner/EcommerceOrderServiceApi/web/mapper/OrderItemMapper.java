package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderItemEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ProcessProductRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toDomain(OrderItemEntity entity);

    List<OrderItem> toDomain(List<OrderItemEntity> entities);

    OrderItemEntity toEntity(OrderItem item);

    List<OrderItemEntity> toEntity(List<OrderItem> items);

}