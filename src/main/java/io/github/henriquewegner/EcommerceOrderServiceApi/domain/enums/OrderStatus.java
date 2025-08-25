package io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums;

public enum OrderStatus {
    CANCELLED,
    FAILED_RESERVATION,
    FAILED_PAYMENT,
    CONFIRMED,
    PAID,
    PENDING_PAYMENT
}
