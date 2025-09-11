package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
}
