package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toDomain(CustomerRequestDTO dto);

    CustomerEntity toEntity(Customer customer);
}
