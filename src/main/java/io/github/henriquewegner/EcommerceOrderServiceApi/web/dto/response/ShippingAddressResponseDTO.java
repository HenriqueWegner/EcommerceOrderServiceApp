package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

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
