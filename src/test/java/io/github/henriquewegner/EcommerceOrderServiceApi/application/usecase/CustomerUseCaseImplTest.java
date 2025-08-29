package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

class CustomerUseCaseImplTest {

    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private CustomerMapper customerMapper;
    private OrderMapper orderMapper;
    private CustomerUseCaseImpl customerUseCase;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        orderRepository = mock(OrderRepository.class);
        customerMapper = mock(CustomerMapper.class);
        orderMapper = mock(OrderMapper.class);
        customerUseCase = new CustomerUseCaseImpl(customerRepository, orderRepository, customerMapper, orderMapper);
    }

    @Test
    void createCustomer_shouldSaveAndReturnId() {
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        Customer customer = mock(Customer.class);
        CustomerEntity entity = mock(CustomerEntity.class);
        UUID id = UUID.randomUUID();

        when(customerMapper.toDomain(dto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(entity);
        when(entity.getId()).thenReturn(id);

        UUID result = customerUseCase.createCustomer(dto);

        assertEquals(id, result);
        verify(customerRepository).save(customer);
    }

    @Test
    void findCustomerOrders_shouldReturnCustomerOrdersResponseDTO_whenCustomerExists() {
        String id = UUID.randomUUID().toString();
        UUID uuid = UUID.fromString(id);
        CustomerEntity customerEntity = mock(CustomerEntity.class);
        List<OrderEntity> orderEntities = Arrays.asList(mock(OrderEntity.class));
        List<Order> orders = Arrays.asList(mock(Order.class));
        Customer customer = mock(Customer.class);
        CustomerOrdersResponseDTO responseDTO = mock(CustomerOrdersResponseDTO.class);

        when(customerRepository.findById(uuid)).thenReturn(Optional.of(customerEntity));
        when(orderRepository.findByCustomer(customerEntity)).thenReturn(orderEntities);
        when(orderMapper.toDomain(orderEntities)).thenReturn(orders);
        when(customerMapper.toDomain(customerEntity)).thenReturn(customer);
        doNothing().when(customer).setOrders(orders);
        when(customerMapper.toDto(customer)).thenReturn(responseDTO);

        Optional<CustomerOrdersResponseDTO> result = customerUseCase.findCustomerOrders(id);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    void findCustomerOrders_shouldReturnEmpty_whenCustomerDoesNotExist() {
        String id = UUID.randomUUID().toString();
        UUID uuid = UUID.fromString(id);

        when(customerRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<CustomerOrdersResponseDTO> result = customerUseCase.findCustomerOrders(id);

        assertFalse(result.isPresent());
        verify(orderRepository, never()).findByCustomer(any());
    }
}