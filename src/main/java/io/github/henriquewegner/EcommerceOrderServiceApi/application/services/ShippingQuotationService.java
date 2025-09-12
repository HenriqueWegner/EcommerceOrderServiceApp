package io.github.henriquewegner.EcommerceOrderServiceApi.application.services;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ShippingQuotation;
import org.springframework.stereotype.Service;

@Service
public class ShippingQuotationService {
    private final ShippingQuotation shippingQuotation;

    public ShippingQuotationService(ShippingQuotation shippingQuotation) {
        this.shippingQuotation = shippingQuotation;
    }

    public void quoteShipping(Order order) {
        Shipping quotation = shippingQuotation.quoteShipping(order.getShippingAddress().getCep());
        order.setShipping(quotation);
    }
}