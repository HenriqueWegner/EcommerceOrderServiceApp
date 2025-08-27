package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;

public interface ShippingQuotation {

    Shipping quoteShipping(String cep);
}
