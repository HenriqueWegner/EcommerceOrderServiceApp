package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.ShippingAddressValueObject;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ShippingAddressDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ShippingAddressResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {

    ShippingAddress toDomain(ShippingAddressDTO dto);
    ShippingAddressValueObject toValueObject(ShippingAddress shippingAddress);
    ShippingAddressResponseDTO toDto(ShippingAddressValueObject valueObject);
}
