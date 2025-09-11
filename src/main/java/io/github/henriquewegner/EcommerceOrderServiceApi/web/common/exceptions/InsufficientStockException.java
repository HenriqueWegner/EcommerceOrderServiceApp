package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
