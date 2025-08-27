package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Shipping {

    private UUID id;
    private String carrier;
    private BigDecimal price;
    private LocalDate estimatedDelivery;
    private String cepOrigin;
    private String cepDestination;
    private Order order;

}
