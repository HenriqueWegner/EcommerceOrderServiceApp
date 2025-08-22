package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrdersResponseDTO(
        UUID id,
        List<OrderItemResponseDTO> items,
        Currency currency,
        PaymentResponseDTO payment,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String idempotencyKey
) {
}
