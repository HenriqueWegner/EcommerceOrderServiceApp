package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ShippingAddressDTO(

        @NotBlank(message = "Campo obrigatorio.")
        String number,
        String complement,
        @NotBlank(message = "Campo obrigatorio.")
        String cep
) {
}
