package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.ShippingAddress;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ViaCepResponse;
import org.apache.kafka.common.errors.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViaCepImplTest {

    private RestTemplate restTemplate;
    private ViaCepImpl viaCep;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        viaCep = new ViaCepImpl(restTemplate);
        ReflectionTestUtils.setField(viaCep, "VIA_CEP_URL", "http://fake-url/%s");
    }

    @Test
    void lookUpByCep_shouldReturnShippingAddress_whenApiReturnsValidResponse() {
        ViaCepResponse response = new ViaCepResponse(
                "12345678",
                "Rua Teste",
                null,
                null,
                "Bairro",
                "Cidade",
                "UF",
                "Estado",
                null
        );
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class)))
                .thenReturn(response);

        ShippingAddress address = viaCep.lookUpByCep("12345678");

        assertNotNull(address);
        assertEquals("12345678", address.getCep());
        assertEquals("Rua Teste", address.getStreet());
        assertEquals("Bairro", address.getNeighborhood());
        assertEquals("Cidade", address.getCity());
        assertEquals("Estado", address.getState());
        assertEquals("UF", address.getUf());
    }

    @Test
    void lookUpByCep_shouldThrowApiException_whenApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class)))
                .thenReturn(null);

        assertThrows(ApiException.class, () -> viaCep.lookUpByCep("12345678"));
    }

    @Test
    void lookUpByCep_shouldThrowApiException_whenApiReturnsResponseWithNullCep() {
        ViaCepResponse response = new ViaCepResponse(
                null,
                "Rua Teste",
                null,
                null,
                "Bairro",
                "Cidade",
                "UF",
                "Estado",
                null
        );
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class)))
                .thenReturn(response);

        assertThrows(ApiException.class, () -> viaCep.lookUpByCep("12345678"));
    }

    @Test
    void lookUpByCep_shouldThrowApiException_whenRestTemplateThrowsException() {
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class)))
                .thenThrow(new RuntimeException("API down"));

        assertThrows(ApiException.class, () -> viaCep.lookUpByCep("12345678"));
    }
}