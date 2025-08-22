package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = CustomerEntity.class)
public abstract class OrderMapper {

    @Autowired
    CustomerRepository customerRepository;

    @Mapping(target="customer", expression = "java(customerRepository.findById(dto.customerId()).orElse(null))")
    public abstract Order toDomain(OrderRequestDTO dto);

    public abstract OrderEntity toEntity(Order order);

    @Mapping(target="customer", expression = "java(customerRepository.findById(dto.customerId()).orElse(null))")
    public abstract OrderEntity toEntity(OrderRequestDTO dto);
}
