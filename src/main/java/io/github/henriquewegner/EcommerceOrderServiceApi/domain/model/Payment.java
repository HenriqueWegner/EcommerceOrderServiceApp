package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class Payment {

    PaymentMethod paymentMethod;
    String cardToken;
    BigDecimal amount;
}
