package io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidEnumException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util.MessageUtil;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT,
    PIX,
    BOLETO;

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        try {
            return PaymentMethod.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumException(MessageUtil.INVALID_PAYMENT_METHOD);
        }
    }
}
