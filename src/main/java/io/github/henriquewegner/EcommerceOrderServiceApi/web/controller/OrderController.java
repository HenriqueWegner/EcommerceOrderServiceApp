package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    public ResponseEntity<CreatedOrderResponseDTO> save(@RequestBody @Valid OrderRequestDTO orderRequestDTO){
        log.info("Creating new order for customer: {}", orderRequestDTO.customerId());

        CreatedOrderResponseDTO response = orderUseCase.createOrder(orderRequestDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable("id") String id){
        log.info("Finding order for id: {}", id);

        return Optional.ofNullable(orderUseCase.findOrder(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PatchMapping("{id}/payment")
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    public ResponseEntity<Void> updatePayment(@PathVariable("id") String id,
                                              @RequestBody @Valid PaymentUpdateRequestDTO paymentUpdateRequestDTO){
        log.info("Updating payment for id: {}", id);

        return orderUseCase.updatePayment(id,paymentUpdateRequestDTO)
                ? ResponseEntity.accepted().build()
                : ResponseEntity.notFound().build();
    }
}
