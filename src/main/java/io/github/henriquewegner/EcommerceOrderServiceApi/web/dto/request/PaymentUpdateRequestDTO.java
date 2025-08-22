package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentUpdateRequestDTO(
        @NotBlank(message = "Campo obrigatorio.")
        String cardToken,
        @NotNull(message = "Campo obrigatorio.")
        PaymentStatus status

) {
}
