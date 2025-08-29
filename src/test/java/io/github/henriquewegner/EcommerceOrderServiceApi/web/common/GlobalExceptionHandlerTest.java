package io.github.henriquewegner.EcommerceOrderServiceApi.web.common;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.DuplicatedRegistryException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidEnumException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ErrorResponse;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.SingleError;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.common.errors.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMethodArgumentNotValidException_returnsUnprocessableEntity() {
        FieldError fieldError = new FieldError("object", "field", "must not be null");
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getFieldErrors()).thenReturn(List.of(fieldError));
        when(ex.getMessage()).thenReturn("Validation failed");

        ErrorResponse response = handler.handleMethodArgumentNotValidException(ex);

        assertEquals(422, response.status());
        assertEquals("Validation error", response.message());
        assertEquals(1, response.errors().size());
        SingleError error = response.errors().get(0);
        assertEquals("field", error.field());
        assertEquals("must not be null", error.error());
    }

    @Test
    void handleDuplicatedRegistryException_returnsConflict() {
        DuplicatedRegistryException ex = new DuplicatedRegistryException("Duplicate");
        ErrorResponse response = handler.handleDuplicatedRegistryException(ex);
        assertEquals(409, response.status());
        assertEquals("Duplicate", response.message());
    }

    @Test
    void handleEntityNotFoundException_returnsUnprocessableEntity() {
        EntityNotFoundException ex = new EntityNotFoundException("Not found");
        ErrorResponse response = handler.handleEntityNotFoundException(ex);
        assertEquals(422, response.status());
        assertEquals("Not found", response.message());
    }

    @Test
    void handleInvalidFieldException_returnsUnprocessableEntity() {
        InvalidFieldException ex = new InvalidFieldException("field", "invalid");
        ErrorResponse response = handler.handleInvalidFieldException(ex);
        assertEquals(422, response.status());
        assertEquals("Validation error.", response.message());
        assertEquals(1, response.errors().size());
        assertEquals("field", response.errors().get(0).field());
        assertEquals("invalid", response.errors().get(0).error());
    }

    @Test
    void handleApiExceptions_returnsInternalServerError() {
        ApiException ex = new ApiException("Kafka error");
        ErrorResponse response = handler.handleApiExceptions(ex);
        assertEquals(500, response.status());
        assertEquals("Kafka error", response.message());
    }

    @Test
    void handleHttpMessageNotReadableException_withInvalidEnum_returnsBadRequest() {
        InvalidEnumException cause = new InvalidEnumException("Invalid enum");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", cause, null);
        ErrorResponse response = handler.handleHttpMessageNotReadableException(ex);
        assertEquals(400, response.status());
        assertEquals("Invalid enum", response.message());
    }

    @Test
    void handleHttpMessageNotReadableException_withOtherCause_returnsBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", new RuntimeException("other"), null);
        ErrorResponse response = handler.handleHttpMessageNotReadableException(ex);
        assertEquals(400, response.status());
        assertEquals("Invalid request payload", response.message());
    }

    @Test
    void handleNonTreatedExceptions_returnsInternalServerError() {
        RuntimeException ex = new RuntimeException("Unexpected");
        ErrorResponse response = handler.handleNonTreatedExceptions(ex);
        assertEquals(500, response.status());
        assertEquals("An unexpected error occurred, contact the admin.", response.message());
    }
}