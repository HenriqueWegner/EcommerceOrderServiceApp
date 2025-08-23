package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderRequestDTO(

        @UUID
        @NotNull(message = "Campo obrigatorio.")
        String customerId,
        @NotEmpty
        @NotNull(message = "Campo obrigatorio.")
        List<ItemRequestDTO> items,
        @NotNull(message = "Campo obrigatorio.")
        Currency currency,
        @NotNull(message = "Campo obrigatorio.")
        PaymentRequestDTO payment,
        @NotBlank(message = "Campo obrigatorio.")
        String idempotencyKey){
}
