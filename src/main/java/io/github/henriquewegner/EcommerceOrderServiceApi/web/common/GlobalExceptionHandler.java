package io.github.henriquewegner.EcommerceOrderServiceApi.web.common;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.DuplicatedRegistryException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.SingleError;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());

        List<FieldError> fieldErrors = e.getFieldErrors();

        List<SingleError> errorsList = fieldErrors.stream().map(fe ->
                new SingleError(fe.getField(),
                        fe.getDefaultMessage())).collect(Collectors.toList());

        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                errorsList);
    }

    @ExceptionHandler(DuplicatedRegistryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedRegistryException(DuplicatedRegistryException e) {
        return ErrorResponse.conflict(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return ErrorResponse.unprocessableEntity(e.getMessage());
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleInvalidFieldException(InvalidFieldException e) {
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error.",
                List.of(new SingleError(e.getField(), e.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNonTreatedExceptions(RuntimeException e){
        log.error("Unexpected error: {}", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred, contact the admin.",
                List.of());
    }
}
