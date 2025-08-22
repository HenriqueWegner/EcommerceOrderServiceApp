package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatedOrderResponseDTO(
        UUID orderId,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
