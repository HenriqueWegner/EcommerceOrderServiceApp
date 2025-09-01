package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;

import java.util.Optional;
import java.util.UUID;

public interface CustomerUseCase {

    UUID createCustomer(CustomerRequestDTO customerRequestDTO);

    Optional<CustomerOrdersResponseDTO> findCustomerOrders(String id);


}
