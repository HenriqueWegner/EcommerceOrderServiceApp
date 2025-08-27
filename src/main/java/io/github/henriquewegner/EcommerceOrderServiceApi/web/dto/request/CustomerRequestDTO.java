package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.PatternUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequestDTO(

        @NotBlank(message = "Obligatory field.")
        @Pattern(regexp = PatternUtil.COMPLETE_NAME, message = MessageUtil.INVALID_NAME)
        String name,
        @NotBlank(message = "Obligatory field.")
        @Email
        String email,
        @Pattern(regexp = PatternUtil.TELEPHONE_NUMBER, message = MessageUtil.INVALID_PHONE)
        String phoneNumber,
        @Pattern(regexp = PatternUtil.FULL_ADDRESS, message = MessageUtil.INVALID_ADDRESS)
        String address
) {
}
