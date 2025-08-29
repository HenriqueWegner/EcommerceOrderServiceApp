package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderIdempotencyId implements Serializable {

    private UUID customerId;
    private String idempotencyKey;

}