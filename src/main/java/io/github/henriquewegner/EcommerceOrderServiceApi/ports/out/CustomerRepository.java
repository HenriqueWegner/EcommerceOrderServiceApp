package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository{
    Optional<CustomerEntity> findById(UUID id);
    CustomerEntity save(Customer customer);
}
