package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.adapters;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Customer;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.CustomerEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.repositories.OrderRepositoryJpa;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.CustomerMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository{

    private final OrderRepositoryJpa orderRepositoryJpa;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderEntity> findByCustomer(CustomerEntity customerEntity) {
        return orderRepositoryJpa.findByCustomer(customerEntity);
    }

    @Override
    public OrderEntity save(Order order){
        OrderEntity entity = orderMapper.toEntity(order);
        entity.linkChildren();
        return orderRepositoryJpa.save(entity);
    }

    @Override
    public Optional<OrderEntity> findById(UUID id) {
        return orderRepositoryJpa.findById(id);
    }

    @Override
    public Optional<OrderEntity> findByIdempotencyKey(String idempotencyKey){

        return orderRepositoryJpa.findByIdempotencyKey(idempotencyKey);
    }


}
