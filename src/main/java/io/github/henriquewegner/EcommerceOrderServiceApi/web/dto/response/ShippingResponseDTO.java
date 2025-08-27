package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ShippingResponseDTO(
        String carrier,
        BigDecimal price,
        LocalDate estimatedDelivery,
        String cepOrigin,
        String cepDestination
) {
}
