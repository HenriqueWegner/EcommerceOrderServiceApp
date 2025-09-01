package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryJpa extends JpaRepository<CustomerEntity, UUID>{

    Optional<CustomerEntity> findByEmail(String email);
}
