package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentMethod;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.PatternUtil;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        @NotNull(message = "Obligatory field.")
        PaymentMethod method,
        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.CARD_TOKEN, message = MessageUtil.INVALID_CARD_TOKEN)
        String cardToken,
        @NotNull(message = "Obligatory field.")
        @PositiveOrZero(message = "Amount must be zero or positive")
        BigDecimal amount
) {
}
