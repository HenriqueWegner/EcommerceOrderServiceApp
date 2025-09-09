package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatternUtil {

    public static final String SKU = "^[A-Z0-9_-]+$";
    public static final String CARD_TOKEN = "^tok_[a-zA-Z0-9]+$";
    public static final String HOUSE_NUMBER = "^\\d+[A-Za-z]?$";
    public static final String HOUSE_COMPLEMENT = "^[A-Za-zÀ-ÿ0-9\\s.,/-]*$";
    public static final String CEP = "^[0-9]{5}(-[0-9]{3})?$";

}
