package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.OrderRepository;
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
public class CustomerService implements CustomerUseCase {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @Override
    public UUID createCustomer(CustomerRequestDTO customerRequestDTO) {

        Customer customer = customerMapper.toDomain(customerRequestDTO);
        CustomerEntity entity = customerMapper.toEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(entity);

        return savedEntity.getId();

    }

    @Override
    public CustomerOrdersResponseDTO findCustomerOrders(String id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(UUID.fromString(id));

        if(customerEntity.isPresent()){
            List<OrderEntity> orderEntities = orderRepository.findByCustomer(customerEntity.get());

            List<Order> orders = orderMapper.toDomain(orderEntities);
            Customer customer = customerMapper.toDomain(customerEntity.get());
            customer.setOrders(orders);

            CustomerOrdersResponseDTO customerDto = customerMapper.toDto(customer);

            return customerDto;

        }
        return null;
    }
}
