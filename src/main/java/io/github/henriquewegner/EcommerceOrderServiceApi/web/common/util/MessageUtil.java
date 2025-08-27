package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtil {

    public static final String INVALID_NAME = "It needs to be a valid real name.";
    public static final String INVALID_PHONE = "Phone number must be valid.";
    public static final String INVALID_CEP = "CEP must be valid.";
    public static final String INVALID_HOUSE_NUMBER = "House number must be valid.";
    public static final String INVALID_HOUSE_COMPLEMENT = "House complement must be valid.";
    public static final String INVALID_ADDRESS = "Address must be valid.";
    public static final String INVALID_SKU = "SKU must be valid.";
    public static final String INVALID_CURRENCY = "Currency must be USD, BRL or EUR.";
    public static final String INVALID_PAYMENT_METHOD = "Payment method must be CREDIT_CARD, DEBIT, PIX or BOLETO.";
    public static final String INVALID_CARD_TOKEN = "Card token is invalid.";
    public static final String INVALID_PAYMENT_STATUS = "Payment status must be PENDING, SUCCESS or FAILED.";

}