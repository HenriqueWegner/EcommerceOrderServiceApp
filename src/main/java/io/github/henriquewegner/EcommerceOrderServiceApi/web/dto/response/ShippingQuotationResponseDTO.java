package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

import java.math.BigDecimal;

public record ShippingQuotationResponseDTO(
        BigDecimal valor,
        Integer prazo,
        String servico,
        String origem,
        String destino
) {
}
