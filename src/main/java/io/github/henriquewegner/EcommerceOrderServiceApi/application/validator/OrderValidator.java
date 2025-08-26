package io.github.henriquewegner.EcommerceOrderServiceApi.application.validator;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.DuplicatedRegistryException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final OrderRepository orderRepository;

    public void validate(Order order){

        validateOrder(order);
        order.getItems().forEach(this::validateItems);
        validatePayment(order.getPayment());
    }

    private void validateOrder(Order order){
        checkIdempotencyKey(order.getIdempotencyKey());
    }

    private void validateItems(OrderItem orderItem){
        validateUnitPrice(orderItem.getUnitPrice());
    }

    private void validatePayment(Payment payment){

    }

    public void checkIdempotencyKey(String idempotencyKey){
        Optional<OrderEntity> entity = orderRepository.findByIdempotencyKey(idempotencyKey);
        if(entity.isPresent()){
            throw new DuplicatedRegistryException("This order has already been processed.");
        }
    }

    public void validateUnitPrice(BigDecimal unitPrice){
        if(unitPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidFieldException("unitPrice","UnitPrice is 0 or smaller than 0.");
        }
    }

}
