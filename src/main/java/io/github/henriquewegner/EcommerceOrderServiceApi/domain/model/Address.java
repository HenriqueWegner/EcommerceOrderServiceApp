package io.github.henriquewegner.EcommerceOrderServiceApi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Address {

    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String uf;
    private String number;
    private String complement;

}
