package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OrderRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OrderRepositoryImplTest {

    private final OrderRepositoryJpa jpa = mock(OrderRepositoryJpa.class);
    private final OrderMapper mapper = mock(OrderMapper.class);
    private final OrderRepositoryImpl repo = new OrderRepositoryImpl(jpa, mapper);



    @Test
    void save_mapsLinksAndSaves() {
        Order order = new Order();
        OrderEntity entity = spy(new OrderEntity());
        when(mapper.toEntity(order)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        assertEquals(entity, repo.save(order));
        verify(entity).linkChildren();
        verify(jpa).save(entity);
    }

    @Test
    void findById_returnsEntity() {
        UUID id = UUID.randomUUID();
        OrderEntity entity = new OrderEntity();
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        assertTrue(repo.findById(id).isPresent());
    }
}