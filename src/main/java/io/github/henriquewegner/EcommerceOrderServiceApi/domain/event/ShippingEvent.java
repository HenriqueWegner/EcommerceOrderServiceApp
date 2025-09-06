package io.github.henriquewegner.EcommerceOrderServiceApi.domain.event;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class ShippingEvent {

    private UUID id;
    private OrderStatus status;

}
