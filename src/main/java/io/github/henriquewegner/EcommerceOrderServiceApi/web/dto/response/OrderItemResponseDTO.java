package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        String sku,
        Integer quantity,
        BigDecimal unitPrice
) {
}
