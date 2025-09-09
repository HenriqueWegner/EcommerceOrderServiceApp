package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerApiResponse;

public interface CustomerApi {

    CustomerApiResponse findCustomer(String id);
}
