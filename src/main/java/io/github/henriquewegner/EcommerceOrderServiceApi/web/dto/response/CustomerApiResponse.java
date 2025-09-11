package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import lombok.Data;

import java.util.List;
import java.util.UUID;

public record CustomerApiResponse(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String address,
        List<Order> orders
) {

}
