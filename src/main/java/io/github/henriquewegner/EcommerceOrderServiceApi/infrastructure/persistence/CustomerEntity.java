package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<OrderEntity> orders;
}
