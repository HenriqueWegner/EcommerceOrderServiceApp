package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Customer {

    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<Order> orders;
}
