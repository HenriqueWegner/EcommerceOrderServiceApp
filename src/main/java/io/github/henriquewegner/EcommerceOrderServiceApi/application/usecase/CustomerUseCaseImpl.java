package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUseCaseImpl implements CustomerUseCase {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @Override
    public UUID createCustomer(CustomerRequestDTO customerRequestDTO) {

        Customer customer = customerMapper.toDomain(customerRequestDTO);

        CustomerEntity savedEntity = customerRepository.save(customer);

        return savedEntity.getId();

    }

    @Override
    public Optional<CustomerOrdersResponseDTO> findCustomerOrders(String id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(UUID.fromString(id));

        if(customerEntity.isPresent()) {
            List<OrderEntity> orderEntities = orderRepository.findByCustomer(customerEntity.get());
            CustomerOrdersResponseDTO customerDto = prepareCustomerDto(orderEntities, customerEntity.get());

            return Optional.of(customerDto);
        }
        return Optional.empty();
    }

    private CustomerOrdersResponseDTO prepareCustomerDto(List<OrderEntity> orderEntities, CustomerEntity customerEntity){
        List<Order> orders = orderMapper.toDomain(orderEntities);
        Customer customer = customerMapper.toDomain(customerEntity);
        customer.setOrders(orders);

        return customerMapper.toDto(customer);
    }

}
