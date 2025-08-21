package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        @NotNull(message = "Campo obrigatorio.")
        PaymentMethod paymentMethod,
        @NotBlank(message = "Campo obrigatorio.")
        String cardToken,
        @NotEmpty(message = "Campo obrigatorio.")
        BigDecimal amount
) {
}
