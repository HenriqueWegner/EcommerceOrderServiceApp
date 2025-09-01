package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository{
    Optional<CustomerEntity> findById(UUID id);
    CustomerEntity save(Customer customer);
    Optional<CustomerEntity> findByEmail(String email);
}
