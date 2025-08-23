package io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions;

public class DuplicatedRegistryException extends RuntimeException {
    public DuplicatedRegistryException(String message) {
        super(message);
    }
}
