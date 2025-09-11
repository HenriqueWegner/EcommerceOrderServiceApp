package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ProductApi;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InsufficientStockException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ProcessProductRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductApiImpl implements ProductApi {

    private final String CLIENT_REGISTRATION_ID = "product-client";

    @Value("${api.url.products}")
    private String PRODUCTS_API_URL;

    private final OAuth2ApiClient apiClient;

    @Override
    public void reserveStock(ProcessProductRequestDTO processProductRequestDTO) {
        try {
            apiClient.callApiPut(CLIENT_REGISTRATION_ID,
                    PRODUCTS_API_URL,
                    processProductRequestDTO);

        } catch (ExternalApiException e) {
            throw new InsufficientStockException("Insufficient stock for product.");
        } catch (RuntimeException e) {
            throw new ExternalApiException("Product API not available");
        }
    }
}
