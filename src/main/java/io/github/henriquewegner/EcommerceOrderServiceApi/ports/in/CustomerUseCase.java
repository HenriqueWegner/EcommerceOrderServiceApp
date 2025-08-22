package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;

import java.util.UUID;

public interface CustomerUseCase {

    UUID createCustomer(Customer customer);
}
