package io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface OrderIdempotencyMapper {


    @Mapping(source = "customerId", target = "id.customerId")
    @Mapping(source = "idempotencyKey", target = "id.idempotencyKey")
    OrderIdempotencyEntity toEntity(OrderRequestDTO dto);

    default CreatedOrderResponseDTO toDTO(String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(value, CreatedOrderResponseDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse input string to DTO", e);

        }
    }

    default String toJson(CreatedOrderResponseDTO dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(dto);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse input DTO to String", e);

        }
    }
}