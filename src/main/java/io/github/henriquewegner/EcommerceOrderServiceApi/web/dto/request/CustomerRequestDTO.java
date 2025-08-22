package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CustomerRequestDTO(
        @NotBlank(message = "Campo obrigatorio.")
        String name,
        @NotBlank(message = "Campo obrigatorio.")
        @Email
        String email,
        String phoneNumber,
        String address
) {
}
