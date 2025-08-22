package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.util.List;
import java.util.UUID;


public record CustomerOrdersResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String address,
        List<OrdersResponseDTO> orders
) {

}
