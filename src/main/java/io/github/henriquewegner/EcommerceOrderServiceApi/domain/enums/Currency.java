package io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidEnumException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;

public enum Currency {
    BRL,
    USD,
    EUR;

    @JsonCreator
    public static Currency fromString(String value) {
        try {
            return Currency.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumException(MessageUtil.INVALID_CURRENCY);
        }
    }
}
