package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Shipping;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.ShippingQuotationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ShippingQuotationImplTest {

    private RestTemplate restTemplate;
    private ShippingQuotationImpl shippingQuotation;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        shippingQuotation = new ShippingQuotationImpl(restTemplate);
        ReflectionTestUtils.setField(shippingQuotation, "SHIPPING_QUOTATION_URL", "http://fake-url");
    }

    @Test
    void quoteShipping_shouldReturnShipping_whenApiReturnsValidResponse() {
        ShippingQuotationResponseDTO responseDTO = new ShippingQuotationResponseDTO(
                new BigDecimal("10.0"), 6, "sedex", "88306773", "12345678"
        );
        when(restTemplate.getForObject(anyString(), eq(ShippingQuotationResponseDTO.class)))
                .thenReturn(responseDTO);

        Shipping shipping = shippingQuotation.quoteShipping("12345678");

        assertNotNull(shipping);
        assertEquals("sedex", shipping.getCarrier());
        assertEquals(new BigDecimal("10.0"), shipping.getPrice());
        assertEquals(LocalDate.now().plusDays(6), shipping.getEstimatedDelivery());
        assertEquals("88306773", shipping.getCepOrigin());
        assertEquals("12345678", shipping.getCepDestination());
    }
}