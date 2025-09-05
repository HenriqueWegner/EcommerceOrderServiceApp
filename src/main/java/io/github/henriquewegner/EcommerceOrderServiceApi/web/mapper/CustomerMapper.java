package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ShippingAddressMapper.class)
public interface CustomerMapper {

    @Mapping(target = "orders", ignore = true)
    Customer toDomain(CustomerEntity entity);

    CustomerEntity toEntity(Customer customer);

}
