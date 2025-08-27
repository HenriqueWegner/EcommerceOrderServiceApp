package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.PatternUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PaymentUpdateRequestDTO(
        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.CARD_TOKEN, message = MessageUtil.INVALID_CARD_TOKEN)
        String cardToken,
        @NotNull(message = "Obligatory field.")
        PaymentStatus status
) {
}
