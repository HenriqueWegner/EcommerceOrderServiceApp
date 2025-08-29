package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.CustomerRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerRepositoryImplTest {

    private final CustomerRepositoryJpa jpa = mock(CustomerRepositoryJpa.class);
    private final CustomerMapper mapper = mock(CustomerMapper.class);
    private final CustomerRepositoryImpl repo = new CustomerRepositoryImpl(jpa, mapper);

    @Test
    void findById_returnsEntity() {
        UUID id = UUID.randomUUID();
        CustomerEntity entity = new CustomerEntity();
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        assertTrue(repo.findById(id).isPresent());
    }

    @Test
    void save_mapsAndSaves() {
        Customer customer = new Customer();
        CustomerEntity entity = new CustomerEntity();
        when(mapper.toEntity(customer)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        assertEquals(entity, repo.save(customer));
        verify(mapper).toEntity(customer);
        verify(jpa).save(entity);
    }
}