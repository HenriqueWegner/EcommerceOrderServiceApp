package io.github.henriquewegner.EcommerceOrderServiceApi.domain.event;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentEvent {

    private UUID id;
    private PaymentOrderEvent order;
    private PaymentMethod method;
    private PaymentStatus paymentStatus;
    private String cardToken;
    private BigDecimal amount;
}
