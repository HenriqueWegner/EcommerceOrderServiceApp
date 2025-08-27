package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;

public interface AddressLookup {

    ShippingAddress lookUpByCep(String cep);
}
