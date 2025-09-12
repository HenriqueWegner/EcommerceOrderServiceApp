package io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api;

import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ProcessProductRequestDTO;

public interface ProductApi {

    void processStock(ProcessProductRequestDTO processProductRequestDTO);

}
