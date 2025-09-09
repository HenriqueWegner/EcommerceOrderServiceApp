//package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;
//
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentOrderEvent;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
//import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
//import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
//import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//
//class PaymentEventHandlerImplTest {
//
//    private OrderRepository orderRepository;
//    private OrderMapper orderMapper;
//    private PaymentEventHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        orderRepository = mock(OrderRepository.class);
//        orderMapper = mock(OrderMapper.class);
//        handler = new PaymentEventHandlerImpl(orderRepository, orderMapper);
//    }
//
//    @Test
//    void handlePaymentUpdate_shouldUpdatePaymentStatusAndCardToken() {
//        UUID orderId = UUID.randomUUID();
//        PaymentStatus statusSuccess = PaymentStatus.SUCCESS;
//        String cardToken = "tok_visa123";
//
//        Order order = mock(Order.class);
//        Payment payment = mock(Payment.class);
//
//        OrderEntity orderEntity = mock(OrderEntity.class);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
//        when(orderMapper.toDomain(orderEntity)).thenReturn(order);
//        when(order.getPayment()).thenReturn(payment);
//        when(payment.getPaymentStatus()).thenReturn(PaymentStatus.PENDING);
//
//        handler.handlePaymentUpdate(orderId,statusSuccess,cardToken);
//
//        verify(payment).setPaymentStatus(statusSuccess);
//        verify(payment).setCardToken(cardToken);
//        verify(orderRepository).save(order);
//    }
//
//    @Test
//    void handlePaymentUpdate_shouldNotUpdateIfAlreadyProcessed() {
//        UUID orderId = UUID.randomUUID();
//        PaymentStatus statusSuccess = PaymentStatus.SUCCESS;
//        PaymentStatus statusFailed = PaymentStatus.FAILED;
//        String cardToken = "tok_visa123";
//        Order order = mock(Order.class);
//        Payment payment = mock(Payment.class);
//        OrderEntity orderEntity = mock(OrderEntity.class);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
//        when(orderMapper.toDomain(orderEntity)).thenReturn(order);
//        when(order.getPayment()).thenReturn(payment);
//
//        // Test for SUCCESS
//        when(payment.getPaymentStatus()).thenReturn(PaymentStatus.SUCCESS);
//        handler.handlePaymentUpdate(orderId,statusSuccess,cardToken);
//        verify(orderRepository, never()).save(any());
//
//        // Test for FAILED
//        reset(payment, orderRepository);
//        when(order.getPayment()).thenReturn(payment);
//        when(payment.getPaymentStatus()).thenReturn(PaymentStatus.FAILED);
//        handler.handlePaymentUpdate(orderId,statusFailed,cardToken);
//        verify(orderRepository, never()).save(any());
//    }
//
//    @Test
//    void handlePaymentUpdate_shouldDoNothingIfOrderNotFound() {
//        UUID orderId = UUID.randomUUID();
//        PaymentStatus statusSuccess = PaymentStatus.SUCCESS;
//        String cardToken = "tok_visa123";
//        Order order = mock(Order.class);
//        PaymentOrderEvent paymentOrderEvent = mock(PaymentOrderEvent.class);
//
//        when(paymentOrderEvent.getId()).thenReturn(orderId);
//        when(order.getId()).thenReturn(orderId);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
//
//        handler.handlePaymentUpdate(orderId,statusSuccess,cardToken);
//
//        verify(orderMapper, never()).toDomain(any(OrderEntity.class));
//        verify(orderRepository, never()).save(any());
//    }
//}