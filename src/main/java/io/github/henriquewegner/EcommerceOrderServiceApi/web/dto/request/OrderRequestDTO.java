package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "Campo obrigatorio.")
        UUID customerId,
        @NotEmpty
        @NotNull(message = "Campo obrigatorio.")
        List<ItemRequestDTO> items,
        @NotNull(message = "Campo obrigatorio.")
        String currency,
        @NotNull(message = "Campo obrigatorio.")
        PaymentRequestDTO payment,
        @NotBlank(message = "Campo obrigatorio.")
        String idempotencyKey){
}
