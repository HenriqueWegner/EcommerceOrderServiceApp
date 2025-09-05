package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    @Operation(summary = "Save", description="Save new order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registered with success."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Order already exists.")
    })
    public ResponseEntity<CreatedOrderResponseDTO> save(@RequestBody @Valid OrderRequestDTO orderRequestDTO){
        log.info("Creating new order for customer: {}", orderRequestDTO.customerId());

        CreatedOrderResponseDTO response = orderUseCase.createOrder(orderRequestDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    @Operation(summary = "Find order", description="Find order by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable("id") String id){
        log.info("Finding order for id: {}", id);

        return orderUseCase.findOrder(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PatchMapping("{id}/payment")
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    @Operation(summary = "Update Payent", description="Update payment status.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Updated with success."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    public ResponseEntity<Object> updatePayment(@PathVariable("id") String id,
                                              @RequestBody @Valid PaymentUpdateRequestDTO paymentUpdateRequestDTO){
        log.info("Updating payment for id: {}", id);

        return orderUseCase.updatePayment(id,paymentUpdateRequestDTO)
                .map(updated -> ResponseEntity.accepted().build())
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAnyRole(@environment.getProperty('security.roles.all-access').split(','))")
    @Operation(summary = "Find orders", description="Find order by customer id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(@PathVariable("id") String customerId){
        log.info("Finding order for customer id: {}", customerId);

        List<OrderResponseDTO> orders = orderUseCase.findOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);    }
}
