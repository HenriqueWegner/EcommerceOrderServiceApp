package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private final OrderUseCase orderUseCase = mock(OrderUseCase.class);
    private final OrderController controller = new OrderController(orderUseCase);

    @Test
    void save_returnsOkWithCreatedOrder() {
        OrderRequestDTO dto = mock(OrderRequestDTO.class);
        when(dto.customerId()).thenReturn("cust1");
        CreatedOrderResponseDTO responseDTO = mock(CreatedOrderResponseDTO.class);
        when(orderUseCase.createOrder(dto)).thenReturn(responseDTO);

        ResponseEntity<CreatedOrderResponseDTO> response = controller.save(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getOrder_found_returnsOk() {
        String id = "order1";
        OrderResponseDTO order = mock(OrderResponseDTO.class);
        when(orderUseCase.findOrder(id)).thenReturn(Optional.of(order));

        ResponseEntity<OrderResponseDTO> response = controller.getOrder(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());
    }

    @Test
    void getOrder_notFound_returnsNotFound() {
        String id = "order1";
        when(orderUseCase.findOrder(id)).thenReturn(Optional.empty());

        ResponseEntity<OrderResponseDTO> response = controller.getOrder(id);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updatePayment_found_returnsAccepted() {
        String id = "order1";
        PaymentUpdateRequestDTO dto = mock(PaymentUpdateRequestDTO.class);
        OrderResponseDTO orderResponse = mock(OrderResponseDTO.class);
        when(orderUseCase.updatePayment(id, dto)).thenReturn(Optional.of(orderResponse));

        ResponseEntity<Object> response = controller.updatePayment(id, dto);

        assertEquals(202, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updatePayment_notFound_returnsNotFound() {
        String id = "order1";
        PaymentUpdateRequestDTO dto = mock(PaymentUpdateRequestDTO.class);
        when(orderUseCase.updatePayment(id, dto)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = controller.updatePayment(id, dto);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}