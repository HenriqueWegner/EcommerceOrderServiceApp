package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record ItemRequestDTO(
        @NotBlank(message = "Campo obrigatorio.")
        String sku,
        @NotEmpty(message = "Campo obrigatorio.")
        Integer quantity,
        @NotEmpty(message = "Campo obrigatorio.")
        BigDecimal unitPrice
) {
}
