package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderRequestDTO(

        @UUID
        @NotNull(message = "Obligatory field.")
        String customerId,
        @NotEmpty
        @NotNull(message = "Obligatory field.")
        @Valid
        List<ItemRequestDTO> items,
        @NotNull(message = "Obligatory field.")
        Currency currency,
        @NotNull(message = "Obligatory field.")
        @Valid
        PaymentRequestDTO payment,
        @NotNull(message = "Obligatory field.")
        @Valid
        ShippingAddressDTO shippingAddress,
        @UUID
        @NotBlank(message = "Obligatory field.")
        String idempotencyKey){
}
