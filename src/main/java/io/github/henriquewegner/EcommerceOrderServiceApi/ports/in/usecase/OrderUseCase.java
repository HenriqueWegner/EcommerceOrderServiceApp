package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;

import java.util.List;
import java.util.Optional;

public interface OrderUseCase {

    CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO);

    Optional<OrderResponseDTO> findOrder(String id);

    List<OrderResponseDTO> findOrdersByCustomer(String customerId);

    Optional<OrderResponseDTO> updatePayment(String id, PaymentUpdateRequestDTO paymentUpdateRequestDTO);

    Optional<OrderResponseDTO> cancelOrder(String id);
}
