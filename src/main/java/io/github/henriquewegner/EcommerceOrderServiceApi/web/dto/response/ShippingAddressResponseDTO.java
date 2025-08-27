package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import jakarta.validation.constraints.NotBlank;

public record ShippingAddressResponseDTO(
        String cep,
        String street,
        String neighborhood,
        String city,
        String state,
        String uf,
        String number,
        String complement
) {
}
