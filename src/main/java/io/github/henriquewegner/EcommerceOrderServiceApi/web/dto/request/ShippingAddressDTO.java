package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.PatternUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShippingAddressDTO(

        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.HOUSE_NUMBER, message = MessageUtil.INVALID_HOUSE_NUMBER)
        String number,
        @Pattern(regexp = PatternUtil.HOUSE_COMPLEMENT, message = MessageUtil.INVALID_HOUSE_COMPLEMENT)
        String complement,
        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.CEP, message = MessageUtil.INVALID_CEP)
        String cep
) {
}
