package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(
        UUID orderId,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
