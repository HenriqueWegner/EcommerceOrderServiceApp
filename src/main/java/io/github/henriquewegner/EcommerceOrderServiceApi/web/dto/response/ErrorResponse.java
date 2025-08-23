package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<SingleError> errors
) {

    public static ErrorResponse conflict(String mensagem){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }

    public static ErrorResponse unprocessableEntity(String mensagem){
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), mensagem, List.of());
    }
}
