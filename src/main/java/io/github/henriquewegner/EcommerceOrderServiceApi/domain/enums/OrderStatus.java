package io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums;

public enum OrderStatus {
    CANCELLED,
    FAILED_RESERVATION,
    FAILED_PAYMENT,
    CONFIRMED,
    PAID,
    RESERVED_STOCK  ,
    PENDING
}
