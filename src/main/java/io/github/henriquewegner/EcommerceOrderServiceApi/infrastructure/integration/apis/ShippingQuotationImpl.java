package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ShippingQuotation;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ShippingQuotationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ShippingQuotationImpl implements ShippingQuotation {

    private final String ORIGIN_CEP = "88306773";
    private final String CARRIER = "sedex";
    private final String WEIGHT = "1";
    private final String ORIGIN_CEP_QUERY = "cep_origem";
    private final String DESTINATION_CEP_QUERY = "cep_destino";
    private final String CARRIER_QUERY = "servico";
    private final String WEIGHT_QUERY = "peso";

    @Value("${api.url.shipping-quotation}")
    private String SHIPPING_QUOTATION_URL;

    private final RestTemplate restTemplate;

    @Override
    public Shipping quoteShipping(String cep) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(SHIPPING_QUOTATION_URL)
                    .queryParam(ORIGIN_CEP_QUERY, ORIGIN_CEP)
                    .queryParam(DESTINATION_CEP_QUERY, cep)
                    .queryParam(CARRIER_QUERY, CARRIER)
                    .queryParam(WEIGHT_QUERY, WEIGHT)
                    .toUriString();

            ShippingQuotationResponseDTO response = restTemplate.getForObject(url, ShippingQuotationResponseDTO.class);

            if (response == null || response.valor() == null) {
                throw new ExternalApiException("Invalid CEP or API not available.");
            }

            return new Shipping(
                    null,
                    response.servico(),
                    response.valor(),
                    LocalDate.now().plusDays(response.prazo()),
                    response.origem(),
                    response.destino(),
                    null
            );
        } catch (Exception e) {
            throw new ExternalApiException("Invalid CEP or API not available.");
        }

    }
}

