package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;

public interface CustomerApi {

    Customer findCustomer(String id);
}
