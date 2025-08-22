package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.SingleError;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<SingleError> errors
) {
}
