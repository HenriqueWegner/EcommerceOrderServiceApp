package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.PaymentEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.ShippingEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingMapper {

    @Mapping(target = "order", ignore = true)
    Shipping toDomain(ShippingEntity entity);
}
