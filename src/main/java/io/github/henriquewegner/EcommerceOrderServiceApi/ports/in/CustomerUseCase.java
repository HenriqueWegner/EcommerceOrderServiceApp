package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;

import java.util.UUID;

public interface CustomerUseCase {

    UUID createCustomer(CustomerRequestDTO customerRequestDTO);

    CustomerOrdersResponseDTO findCustomerOrders(String id);
}
