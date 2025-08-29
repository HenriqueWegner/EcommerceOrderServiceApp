package io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;

import java.util.Optional;

public interface OrderUseCase {

    CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO);

    Optional<OrderResponseDTO> findOrder(String id);

    Optional<OrderResponseDTO> updatePayment(String id, PaymentUpdateRequestDTO paymentUpdateRequestDTO);
}
