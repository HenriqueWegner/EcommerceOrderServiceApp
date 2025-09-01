package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.validator.OrderValidator;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.*;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.AddressLookup;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ShippingQuotation;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.CustomerRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderIdempotencyRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OutboxRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderIdempotencyMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderUseCaseImplTest {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private OutboxRepository outboxRepository;
    private OrderIdempotencyRepository idempotencyRepository;
    private OrderMapper orderMapper;
    private CustomerMapper customerMapper;
    private PaymentMapper paymentMapper;
    private OrderIdempotencyMapper orderIdempotencyMapper;
    private OrderValidator orderValidator;
    private AddressLookup addressLookup;
    private ShippingQuotation shippingQuotation;
    private OrderUseCaseImpl orderUseCase;
    private PaymentEventHandler paymentEventHandler;

    @BeforeEach
    void setUp(){
        orderRepository = mock(OrderRepository.class);
        customerRepository = mock(CustomerRepository.class);
        outboxRepository = mock(OutboxRepository.class);
        idempotencyRepository = mock(OrderIdempotencyRepository.class);
        orderMapper = mock(OrderMapper.class);
        customerMapper = mock(CustomerMapper.class);
        paymentMapper = mock(PaymentMapper.class);
        orderIdempotencyMapper = mock(OrderIdempotencyMapper.class);
        orderValidator = mock(OrderValidator.class);
        addressLookup = mock(AddressLookup.class);
        shippingQuotation = mock(ShippingQuotation.class);
        paymentEventHandler = mock(PaymentEventHandler.class);
        orderUseCase = new OrderUseCaseImpl(
                orderRepository,
                customerRepository,
                outboxRepository,
                idempotencyRepository,
                orderMapper,
                customerMapper,
                paymentMapper,
                orderIdempotencyMapper,
                orderValidator,
                addressLookup,
                shippingQuotation,
                paymentEventHandler
        );
    }
    @Test
    void createOrder_shouldSaveOrderAndReturnResponse() {
        OrderRequestDTO orderDTO = mock(OrderRequestDTO.class);
        String customerId = UUID.randomUUID().toString();
        String idempotencyKey = UUID.randomUUID().toString();
        when(orderDTO.customerId()).thenReturn(customerId);
        when(orderDTO.idempotencyKey()).thenReturn(idempotencyKey);

        when(idempotencyRepository.findByIdCustomerIdAndIdIdempotencyKey(any(), any()))
                .thenReturn(Optional.empty());

        Customer customer = mock(Customer.class);
        when(customerRepository.findById(any())).thenReturn(Optional.of(mock(io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity.class)));
        when(customerMapper.toDomain(any(CustomerEntity.class))).thenReturn(customer);

        Order order = mock(Order.class);
        when(orderMapper.toDomain(orderDTO, customer)).thenReturn(order);


        Payment payment = mock(Payment.class);
        doNothing().when(orderValidator).validate(order);
        doNothing().when(order).setStatus(any());
        doNothing().when(order).setShippingAddress(any());
        doNothing().when(order).setShipping(any());
        when(order.getPayment()).thenReturn(payment);
        doNothing().when(payment).setPaymentStatus(any());
        ShippingAddress shippingAddress = mock(ShippingAddress.class);
        when(order.getShippingAddress()).thenReturn(shippingAddress);
        when(addressLookup.lookUpByCep(any())).thenReturn(shippingAddress);
        Shipping shipping = mock(Shipping.class);
        when(shippingQuotation.quoteShipping(any())).thenReturn(shipping);

        OrderEntity savedEntity = mock(OrderEntity.class);
        when(savedEntity.getId()).thenReturn(UUID.randomUUID());
        when(orderRepository.save(order)).thenReturn(savedEntity);

        CreatedOrderResponseDTO responseDTO = mock(CreatedOrderResponseDTO.class);
        when(orderMapper.orderEntityToCreatedOrderResponseDTO(savedEntity)).thenReturn(responseDTO);

        when(orderIdempotencyMapper.toJson(responseDTO)).thenReturn("json");
        OrderIdempotencyEntity idempotencyEntity = mock(OrderIdempotencyEntity.class);
        when(orderIdempotencyMapper.toEntity(orderDTO)).thenReturn(idempotencyEntity);

        CreatedOrderResponseDTO result = orderUseCase.createOrder(orderDTO);

        assertEquals(responseDTO, result);
        verify(orderRepository).save(order);
        verify(outboxRepository, times(2)).save(any());
        verify(idempotencyRepository).save(idempotencyEntity);
    }

    @Test
    void createOrder_shouldReturnStoredResponse_whenIdempotencyIsPresent() {
        OrderRequestDTO orderDTO = mock(OrderRequestDTO.class);
        String customerId = UUID.randomUUID().toString();
        String idempotencyKey = UUID.randomUUID().toString();
        String requestHash = String.valueOf(java.util.Objects.hash(orderDTO));
        when(orderDTO.customerId()).thenReturn(customerId);
        when(orderDTO.idempotencyKey()).thenReturn(idempotencyKey);

        OrderIdempotencyEntity idempotencyEntity = mock(OrderIdempotencyEntity.class);
        when(idempotencyEntity.getRequestHash()).thenReturn(requestHash);
        String storedResponseJson = "stored-response-json";
        when(idempotencyEntity.getResponse()).thenReturn(storedResponseJson);

        when(idempotencyRepository.findByIdCustomerIdAndIdIdempotencyKey(
                UUID.fromString(customerId), idempotencyKey))
                .thenReturn(Optional.of(idempotencyEntity));

        CreatedOrderResponseDTO storedResponse = mock(CreatedOrderResponseDTO.class);
        when(orderIdempotencyMapper.toDTO(storedResponseJson)).thenReturn(storedResponse);

        CreatedOrderResponseDTO result = orderUseCase.createOrder(orderDTO);

        assertEquals(storedResponse, result);
        verify(orderRepository, never()).save(any());
        verify(outboxRepository, never()).save(any());
        verify(idempotencyRepository, never()).save(any(OrderIdempotencyEntity.class));
    }

    @Test
    void updatePayment_shouldUpdatePaymentAndReturnOrderResponseDTO_whenOrderExists() {
        String orderId = UUID.randomUUID().toString();
        PaymentUpdateRequestDTO paymentUpdateRequestDTO = mock(PaymentUpdateRequestDTO.class);
        Order order = mock(Order.class);
        OrderEntity savedEntity = mock(OrderEntity.class);
        OrderResponseDTO responseDTO = mock(OrderResponseDTO.class);
        Payment payment = mock(Payment.class);

        when(order.getPayment()).thenReturn(payment);
        when(orderMapper.toDto(savedEntity)).thenReturn(responseDTO);
        when(paymentEventHandler.handlePaymentUpdate(any(),any(),any())).thenReturn(Optional.of(savedEntity));

        Optional<OrderResponseDTO> result = orderUseCase.updatePayment(orderId, paymentUpdateRequestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(paymentEventHandler).handlePaymentUpdate(any(),any(),any());
        verify(orderMapper).toDto(savedEntity);
    }

    @Test
    void updatePayment_shouldReturnEmpty_whenOrderDoesNotExist() {
        String orderId = UUID.randomUUID().toString();
        PaymentUpdateRequestDTO paymentUpdateRequestDTO = mock(PaymentUpdateRequestDTO.class);

        when(paymentEventHandler.handlePaymentUpdate(any(),any(),any())).thenReturn(Optional.empty());

        Optional<OrderResponseDTO> result = orderUseCase.updatePayment(orderId, paymentUpdateRequestDTO);

        assertFalse(result.isPresent());
        verify(paymentEventHandler).handlePaymentUpdate(any(),any(),any());
        verify(orderMapper, never()).toDto(any());
    }


}