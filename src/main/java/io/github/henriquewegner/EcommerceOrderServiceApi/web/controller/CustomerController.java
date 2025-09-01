package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
@Tag(name = "Customers")
public class CustomerController implements GenericController{

    private final CustomerUseCase customerUseCase;

    @PostMapping
    @PreAuthorize("hasRole(@environment.getProperty('security.roles.admin-access'))")
    @Operation(summary = "Save", description="Save new customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered with success."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
    })
    public ResponseEntity<Void> save(@RequestBody @Valid CustomerRequestDTO customerRequestDTO){
        log.info("Creating new customer named: {}", customerRequestDTO.name());

        UUID customerId = customerUseCase.createCustomer(customerRequestDTO);
        URI location = gerarHeaderLocation(customerId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}/orders")
    @PreAuthorize("hasRole(@environment.getProperty('security.roles.admin-access'))")
    @Operation(summary = "Find Customer Orders", description="Find all customer orders.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer orders found."),
            @ApiResponse(responseCode = "404", description = "Customer not found."),
    })
    public ResponseEntity<CustomerOrdersResponseDTO> findCustomerOrders(
            @PathVariable("id") String id){

        return customerUseCase.findCustomerOrders(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);

    }

}
