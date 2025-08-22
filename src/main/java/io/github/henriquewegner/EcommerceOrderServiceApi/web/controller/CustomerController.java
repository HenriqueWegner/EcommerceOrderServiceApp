package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController implements GenericController{

    private final CustomerUseCase customerUseCase;
    private final CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CustomerRequestDTO customerRequestDTO){
        log.info("Creating new customer named: {}", customerRequestDTO.name());

        Customer customer = customerMapper.toDomain(customerRequestDTO);

        UUID customerId = customerUseCase.createCustomer(customer);

        URI location = gerarHeaderLocation(customerId);

        return ResponseEntity.created(location).build();
    }
}
