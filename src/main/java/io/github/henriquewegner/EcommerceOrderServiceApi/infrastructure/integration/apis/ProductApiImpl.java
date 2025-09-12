package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ProductApi;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InsufficientStockException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ProcessProductRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductApiImpl implements ProductApi {

    private final String CLIENT_REGISTRATION_ID = "product-client";

    @Value("${api.url.products}")
    private String PRODUCTS_API_URL;

    private final OAuth2ApiClient apiClient;

    @Override
    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void processStock(ProcessProductRequestDTO processProductRequestDTO) {
        try {
            apiClient.callApiPut(CLIENT_REGISTRATION_ID,
                    PRODUCTS_API_URL,
                    processProductRequestDTO);

        } catch (ExternalApiException e) {
            throw new InsufficientStockException("Insufficient stock for product.");
        }
    }

    @Recover
    public void recover(Exception e, ProcessProductRequestDTO processProductRequestDTO) {
        throw new ExternalApiException("Failed after 3 attempts: Product API not available");
    }
}
