package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<CreatedOrderResponseDTO> save(@RequestBody @Valid OrderRequestDTO orderRequestDTO){
        log.info("Creating new order for customer: {}", orderRequestDTO.customerId());

        CreatedOrderResponseDTO response = orderUseCase.createOrder(orderRequestDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable("id") String id){
        log.info("Finding order for id: {}", id);

        OrderResponseDTO orderResponseDTO = orderUseCase.findOrder(id);

        if(orderResponseDTO != null){
            return ResponseEntity.ok(orderResponseDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
