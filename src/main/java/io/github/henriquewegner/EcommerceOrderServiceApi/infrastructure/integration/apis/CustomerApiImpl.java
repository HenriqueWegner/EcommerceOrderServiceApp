package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.CustomerApi;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.CustomerApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerApiImpl implements CustomerApi {

    private final String CLIENT_REGISTRATION_ID = "customer-client";

    @Value("${api.url.customers}")
    private String CUSTOMERS_API_URL;

    private final OAuth2ApiClient apiClient;

    @Override
    public Customer findCustomer(String id) {

        try {
            return apiClient.callApi(CLIENT_REGISTRATION_ID,
                    CUSTOMERS_API_URL + id,
                    Customer.class);
        }catch(RuntimeException e){
            throw new CustomerApiException("Customer API not available.");
        }
    }
}
