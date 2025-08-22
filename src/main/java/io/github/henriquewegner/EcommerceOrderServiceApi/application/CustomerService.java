package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public UUID createCustomer(Customer customer) {

        CustomerEntity entity = customerMapper.toEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(entity);

        return savedEntity.getId();

    }
}
