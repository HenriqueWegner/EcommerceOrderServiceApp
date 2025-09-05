package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.Currency;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        CustomerResponseDTO customer,
        List<OrderItemResponseDTO>items,
        Currency currency,
        PaymentResponseDTO payment,
        ShippingAddressResponseDTO shippingAddress,
        ShippingResponseDTO shipping,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
