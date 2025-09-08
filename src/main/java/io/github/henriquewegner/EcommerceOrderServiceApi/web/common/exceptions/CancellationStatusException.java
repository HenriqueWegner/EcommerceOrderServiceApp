package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions;

public class CancellationStatusException extends RuntimeException {
    public CancellationStatusException(String message) {
        super(message);
    }
}
