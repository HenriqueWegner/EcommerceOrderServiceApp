package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.PatternUtil;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ItemRequestDTO(
        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.SKU, message = MessageUtil.INVALID_SKU)
        String sku,
        @NotNull(message = "Obligatory field.")
        @Positive(message = "Quantity must be positive")
        Integer quantity,
        @NotNull(message = "Obligatory field.")
        @Positive(message = "Unit must be positive")
        BigDecimal unitPrice
) {
}
