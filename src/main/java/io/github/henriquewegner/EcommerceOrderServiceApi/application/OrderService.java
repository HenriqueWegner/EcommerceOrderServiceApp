package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.validator.OrderValidator;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.PaymentMapper;
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
    private final PaymentMapper paymentMapper;
    private final OrderValidator orderValidator;

    @Override
    public CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO) {

        CustomerEntity customerEntity = customerRepository
                .findById(UUID.fromString(orderDTO.customerId()))
                .orElseThrow(() -> new EntityNotFoundException("Customer not found."));

        Customer customer = customerMapper.toDomain(customerEntity);
        Order order = orderMapper.toDomain(orderDTO, customer);

        orderValidator.validate(order);
        setInitialStatus(order);

        OrderEntity savedEntity = orderRepository.save(order);

        CreatedOrderResponseDTO response =
                orderMapper.orderEntityToCreatedOrderResponseDTO(savedEntity);

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

    @Override
    public boolean updatePayment(String id, PaymentUpdateRequestDTO paymentUpdateRequestDTO) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(UUID.fromString(id));

        if(orderEntity.isPresent()){
            Order order = orderMapper.toDomain(orderEntity.get());

            order.getPayment().setPaymentStatus(paymentUpdateRequestDTO.status());
            order.getPayment().setCardToken(paymentUpdateRequestDTO.cardToken());

             orderRepository.save(order);
             return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void setInitialStatus(Order order){
        order.getPayment().setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.CONFIRMED);
    }
}
