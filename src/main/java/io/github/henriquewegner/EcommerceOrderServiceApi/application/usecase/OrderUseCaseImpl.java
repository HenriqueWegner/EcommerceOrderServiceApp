package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.util.JsonUtil;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.validator.OrderValidator;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Address;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.AddressLookup;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.publisher.EventPublisher;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.PaymentMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.ShippingAddressMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderUseCaseImpl implements io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final PaymentMapper paymentMapper;
    private final ShippingAddressMapper shippingAddressMapper;
    private final OrderValidator orderValidator;
    private final OutboxRepository outboxRepository;
    private final AddressLookup addressLookup;


    @Override
    public CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO) {

        Customer customer = findCustomer(orderDTO.customerId());
        Order order = prepareOrder(orderDTO, customer);
        OrderEntity savedEntity = orderRepository.save(order);
        saveOutboxEvents(savedEntity);

        return orderMapper.orderEntityToCreatedOrderResponseDTO(savedEntity);
    }

    @Override
    public OrderResponseDTO findOrder(String id) {

        return orderRepository.findById(UUID.fromString(id))
                .map(orderMapper::toDto)
                .orElse(null);
    }

    @Override
    public boolean updatePayment(String id, PaymentUpdateRequestDTO paymentUpdateRequestDTO) {

        return orderRepository.findById(UUID.fromString(id))
                .map(orderEntity -> {
                    Order order = prepareOrderPayment(paymentUpdateRequestDTO, orderEntity);
                    orderRepository.save(order);

                    return Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);
    }

    private Customer findCustomer(String customerId){

        CustomerEntity customerEntity = customerRepository
                .findById(UUID.fromString(customerId))
                .orElseThrow(() -> new EntityNotFoundException("Customer not found."));

        return customerMapper.toDomain(customerEntity);
    }

    private Order prepareOrder(OrderRequestDTO orderDTO, Customer customer){

        Order order = orderMapper.toDomain(orderDTO, customer);
        orderValidator.validate(order);
        setInitialStatus(order);
        enrichAddress(order);

        return order;
    }

    private Order enrichAddress(Order order) {
        ShippingAddress address = addressLookup.lookUpByCep(order.getShippingAddress().getCep());
        address.setNumber(order.getShippingAddress().getNumber());
        address.setComplement(order.getShippingAddress().getComplement());
        order.setShippingAddress(address);

        return order;
    }

    private void setInitialStatus(Order order){

        order.getPayment().setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
    }

    private void saveOutboxEvents(OrderEntity orderEntity){

        OutboxEventEntity orderCreatedEvent = new OutboxEventEntity();
        orderCreatedEvent.setAggregateId(orderEntity.getId().toString());
        orderCreatedEvent.setEventType(EventType.ORDER_CREATED);
        orderCreatedEvent.setPayload(orderMapper.toEvent(orderEntity));
        outboxRepository.save(orderCreatedEvent);

        OutboxEventEntity paymentEvent = new OutboxEventEntity();
        paymentEvent.setAggregateId(orderEntity.getId().toString());
        paymentEvent.setEventType(EventType.PAYMENT_EVENT);
        paymentEvent.setPayload(paymentMapper.toEvent(orderEntity.getPayment()));
        outboxRepository.save(paymentEvent);

        outboxRepository.save(orderCreatedEvent);
        outboxRepository.save(paymentEvent);
    }

    private Order prepareOrderPayment(PaymentUpdateRequestDTO paymentUpdateRequestDTO,
                                      OrderEntity orderEntity){

        Order order = orderMapper.toDomain(orderEntity);
        order.getPayment().setPaymentStatus(paymentUpdateRequestDTO.status());
        order.getPayment().setCardToken(paymentUpdateRequestDTO.cardToken());

        return order;
    }

}
