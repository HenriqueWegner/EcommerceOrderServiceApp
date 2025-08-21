package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Payment{

    private UUID id;
    private OrderEntity order;
    private PaymentMethod method;
    private PaymentStatus paymentStatus;
    private String cardToken;
    private BigDecimal amount;
}
