package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderItemEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.PaymentEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderItemResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface OrderMapper {


    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "id", ignore = true)
    Order toDomain(OrderRequestDTO dto, Customer customer);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    OrderEntity toEntity(Order order);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    List<Order> toDomain(List<OrderEntity> entities);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    OrderResponseDTO toDto(OrderEntity entity);

    @Mapping(target = "order", ignore = true)
    OrderItem orderItemEntitytoOrderItem(OrderItemEntity entity);

    @Mapping(target = "order", ignore = true)
    Payment paymentEntityToPayment(PaymentEntity entity);

    List<OrderItem> orderItemEntityListToOrderItemList(List<OrderItemEntity> entities);
}
