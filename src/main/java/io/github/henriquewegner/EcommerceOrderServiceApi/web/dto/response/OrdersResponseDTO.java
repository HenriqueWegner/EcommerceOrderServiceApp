package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrdersResponseDTO(
        UUID id,
        List<OrderItemResponseDTO> items,
        Currency currency,
        PaymentResponseDTO payment,
        OrderStatus status,
        ShippingAddressResponseDTO shippingAddress,
        ShippingResponseDTO shipping,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String idempotencyKey
) {
}
