package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Address;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

public interface AddressLookup {

    ShippingAddress lookUpByCep(String cep);
}
