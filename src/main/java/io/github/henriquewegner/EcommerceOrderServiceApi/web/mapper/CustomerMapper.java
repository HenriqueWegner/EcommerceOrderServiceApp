package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toDomain(CustomerRequestDTO dto);

    @Mapping(target = "orders", ignore = true)
    Customer toDomain(CustomerEntity entity);

    CustomerEntity toEntity(Customer customer);

    CustomerOrdersResponseDTO toDto(Customer customer);
}
