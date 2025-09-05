package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions;

public class CustomerApiException extends RuntimeException {
    public CustomerApiException(String message) {
        super(message);
    }
}
