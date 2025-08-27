package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatedShippingResponseDTO(
        BigDecimal price,
        LocalDate estimatedDelivery
) {
}
