package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(
        UUID orderId,
        Status status,
        LocalDateTime createdAt
) {
}
