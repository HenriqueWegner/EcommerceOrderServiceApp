package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class ShippingAddressValueObject {

    @Column(name = "cep")
    private String cep;

    @Column(name = "street")
    private String street;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "uf")
    private String uf;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;
}
