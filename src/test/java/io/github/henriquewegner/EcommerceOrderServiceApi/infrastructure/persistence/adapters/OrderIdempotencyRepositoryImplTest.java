package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OrderIdempotencyRepositoryJpa;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderIdempotencyRepositoryImplTest {

    private final OrderIdempotencyRepositoryJpa jpa = mock(OrderIdempotencyRepositoryJpa.class);
    private final OrderIdempotencyRepositoryImpl repo = new OrderIdempotencyRepositoryImpl(jpa);

    @Test
    void findByIdCustomerIdAndIdIdempotencyKey_returnsEntity() {
        UUID customerId = UUID.randomUUID();
        String key = "key";
        OrderIdempotencyEntity entity = new OrderIdempotencyEntity();
        when(jpa.findByIdCustomerIdAndIdIdempotencyKey(customerId, key)).thenReturn(Optional.of(entity));
        assertTrue(repo.findByIdCustomerIdAndIdIdempotencyKey(customerId, key).isPresent());
    }

    @Test
    void save_savesEntity() {
        OrderIdempotencyEntity entity = new OrderIdempotencyEntity();
        when(jpa.save(entity)).thenReturn(entity);
        assertEquals(entity, repo.save(entity));
        verify(jpa).save(entity);
    }
}