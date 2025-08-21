package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ItemRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentRequestDTO;

import java.util.List;
import java.util.UUID;

public class Order {

    UUID customerId;
    List<ItemRequestDTO> items;
    String currency;
    PaymentRequestDTO payment;
    String idempotencyKey;
}
