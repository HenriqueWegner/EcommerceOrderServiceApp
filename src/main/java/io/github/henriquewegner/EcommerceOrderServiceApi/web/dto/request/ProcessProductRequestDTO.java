package io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.RequestType;

import java.util.List;

public record ProcessProductRequestDTO(
        RequestType type,
        List<ReservedItemRequestDTO> items
) {

}
