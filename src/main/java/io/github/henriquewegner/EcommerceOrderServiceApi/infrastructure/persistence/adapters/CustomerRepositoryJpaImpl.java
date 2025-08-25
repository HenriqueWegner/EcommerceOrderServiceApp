package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.CustomerRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryJpaImpl implements CustomerRepository{

    private final CustomerRepositoryJpa customerRepositoryJpa;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerEntity> findById(UUID id) {
        return customerRepositoryJpa.findById(id);
    }

    @Override
    public CustomerEntity save(Customer customer){
        CustomerEntity entity = customerMapper.toEntity(customer);
        return customerRepositoryJpa.save(entity);
    }

    
}
