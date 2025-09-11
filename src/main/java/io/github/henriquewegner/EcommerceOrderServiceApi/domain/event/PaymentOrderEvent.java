package io.github.henriquewegner.EcommerceOrderServiceApi.domain.event;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentOrderEvent {

    private UUID id;
}
