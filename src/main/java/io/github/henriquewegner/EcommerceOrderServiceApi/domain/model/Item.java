package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public class Item {

    String sku;
    Integer quantity;
    BigDecimal unitPrice;
}
