package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

public record SingleError(
        String field,
        String error) {
}
