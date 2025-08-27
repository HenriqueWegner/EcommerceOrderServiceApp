package io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidEnumException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED;

    @JsonCreator
    public static PaymentStatus fromString(String value) {
        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumException(MessageUtil.INVALID_PAYMENT_STATUS);
        }
    }
}
