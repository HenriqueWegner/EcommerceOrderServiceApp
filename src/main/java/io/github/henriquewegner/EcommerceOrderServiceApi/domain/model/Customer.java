package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {

    private UUID id;
    private String name;
}
