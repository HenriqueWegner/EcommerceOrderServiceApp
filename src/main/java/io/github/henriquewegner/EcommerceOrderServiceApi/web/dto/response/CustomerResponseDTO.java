package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String address
) {
}
