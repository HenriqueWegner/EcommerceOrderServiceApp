package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final OrderValidator orderValidator;

    @Override
    public CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO) {
        CustomerEntity customerEntity = customerRepository.findById(orderDTO.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Customer customer = customerMapper.toDomain(customerEntity);

        Order order = orderMapper.toDomain(orderDTO, customer);

        orderValidator.validate(order);

        order.getPayment().setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.CONFIRMED);

        OrderEntity orderEntity = orderMapper.toEntity(order);

        OrderEntity persistedEntity = orderRepository.save(orderEntity);

        CreatedOrderResponseDTO response = new CreatedOrderResponseDTO(
                persistedEntity.getId(),
                persistedEntity.getStatus(),
                persistedEntity.getCreatedAt());

        return response;
    }

    @Override
    public OrderResponseDTO findOrder(String id) {

        Optional<OrderEntity> orderEntity = orderRepository.findById(UUID.fromString(id));

        if(orderEntity.isPresent()){
            OrderResponseDTO orderResponseDTO = orderMapper.toDto(orderEntity.get());
            return orderResponseDTO;
        }
        return null;
    }
}
