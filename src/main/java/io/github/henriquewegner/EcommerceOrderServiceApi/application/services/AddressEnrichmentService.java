package io.github.henriquewegner.EcommerceOrderServiceApi.application.services;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.AddressLookup;
import org.springframework.stereotype.Service;

@Service
public class AddressEnrichmentService {
    private final AddressLookup addressLookup;

    public AddressEnrichmentService(AddressLookup addressLookup) {
        this.addressLookup = addressLookup;
    }

    public void enrichAddress(Order order) {
        ShippingAddress address = addressLookup.lookUpByCep(order.getShippingAddress().getCep());
        address.setNumber(order.getShippingAddress().getNumber());
        address.setComplement(order.getShippingAddress().getComplement());
        order.setShippingAddress(address);
    }
}