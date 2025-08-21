package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
        @NotBlank(message = "Campo obrigatorio.")
        UUID customerId,
        @NotEmpty
        @NotNull
        List<ItemRequestDTO> items,
        @NotBlank(message = "Campo obrigatorio.")
        String currency,
        @NotNull
        PaymentRequestDTO payment,
        @NotBlank(message = "Campo obrigatorio.")
        String idempotencyKey){
}
