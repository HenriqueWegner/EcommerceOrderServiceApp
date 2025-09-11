//package io.github.henriquewegner.EcommerceOrderServiceApi.application.validator;
//
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
//import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
//import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
//import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class OrderValidatorTest {
//
//    private OrderRepository orderRepository;
//    private OrderValidator orderValidator;
//
//    @BeforeEach
//    void setUp() {
//        orderRepository = mock(OrderRepository.class);
//        orderValidator = new OrderValidator(orderRepository);
//    }
//
//    @Test
//    void validateUnitPrice_shouldThrowException_whenUnitPriceIsZero() {
//        assertThrows(InvalidFieldException.class, () -> orderValidator.validateUnitPrice(BigDecimal.ZERO));
//    }
//
//    @Test
//    void validateUnitPrice_shouldThrowException_whenUnitPriceIsNegative() {
//        assertThrows(InvalidFieldException.class, () -> orderValidator.validateUnitPrice(BigDecimal.valueOf(-10)));
//    }
//
//    @Test
//    void validateUnitPrice_shouldNotThrow_whenUnitPriceIsPositive() {
//        assertDoesNotThrow(() -> orderValidator.validateUnitPrice(BigDecimal.valueOf(10)));
//    }
//
//    // Example structure for validate(Order order) test
//    @Test
//    void validate_shouldCallAllValidationMethods() {
//        OrderItem item = mock(OrderItem.class);
//        when(item.getUnitPrice()).thenReturn(BigDecimal.valueOf(5));
//        Payment payment = mock(Payment.class);
//        Order order = mock(Order.class);
//        when(order.getItems()).thenReturn(Collections.singletonList(item));
//        when(order.getPayment()).thenReturn(payment);
//
//        assertDoesNotThrow(() -> orderValidator.validate(order));
//    }
//}