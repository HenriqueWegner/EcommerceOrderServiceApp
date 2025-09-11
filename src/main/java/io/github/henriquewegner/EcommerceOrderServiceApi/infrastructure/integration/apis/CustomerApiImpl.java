package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerApiResponse;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.CustomerApi;
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
    public CustomerApiResponse findCustomer(String id) {
        try {
            CustomerApiResponse customerApiResponse = apiClient.callApiGet(CLIENT_REGISTRATION_ID,
                    CUSTOMERS_API_URL + id,
                    CustomerApiResponse.class);

            return customerApiResponse;

        }catch(RuntimeException e){
            throw new ExternalApiException("Customer API not available.");
        }
    }
}
