package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController implements GenericController{

    private final CustomerUseCase customerUseCase;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CustomerRequestDTO customerRequestDTO){
        log.info("Creating new customer named: {}", customerRequestDTO.name());

        UUID customerId = customerUseCase.createCustomer(customerRequestDTO);
        URI location = gerarHeaderLocation(customerId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}/orders")
    public ResponseEntity<CustomerOrdersResponseDTO> findCustomerOrders(
            @PathVariable("id") String id){

        return Optional.ofNullable(customerUseCase.findCustomerOrders(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);

    }

}
