package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

public record SingleError(
        String field,
        String error) {
}
