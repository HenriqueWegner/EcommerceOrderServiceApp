package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String complement,
        String unidade,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao
) {
}
