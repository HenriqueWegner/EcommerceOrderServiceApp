package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OutboxEventEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OutboxRepositoryJpa;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OutboxRepositoryImplTest {

    private final OutboxRepositoryJpa jpa = mock(OutboxRepositoryJpa.class);
    private final OutboxRepositoryImpl repo = new OutboxRepositoryImpl(jpa);

    @Test
    void save_savesEntity() {
        OutboxEventEntity entity = new OutboxEventEntity();
        when(jpa.save(entity)).thenReturn(entity);
        assertEquals(entity, repo.save(entity));
        verify(jpa).save(entity);
    }

    @Test
    void findByProcessed_returnsList() {
        List<OutboxEventEntity> events = List.of(new OutboxEventEntity());
        when(jpa.findByProcessed(true)).thenReturn(events);
        assertEquals(events, repo.findByProcessed(true));
    }
}