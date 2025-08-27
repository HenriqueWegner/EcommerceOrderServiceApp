package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Address;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.AddressLookup;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ViaCepImpl implements AddressLookup {

    @Value("${api.url.via-cep}")
    private String VIA_CEP_URL;

    private final RestTemplate restTemplate;

    @Override
    public ShippingAddress lookUpByCep(String cep) {
        String url = String.format(VIA_CEP_URL,cep);

        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (response == null || response.cep() == null){
            throw new RuntimeException("Cep invalido ou API indisponivel.");
        }

        return new ShippingAddress(
                response.cep(),
                response.logradouro(),
                response.bairro(),
                response.localidade(),
                response.estado(),
                response.uf(),
                null,
                null
        );
    }
}
