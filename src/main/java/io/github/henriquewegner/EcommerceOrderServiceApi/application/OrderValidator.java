package io.github.henriquewegner.EcommerceOrderServiceApi.application;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Payment;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    public void validate(Order order){

        validateOrder(order);
        validatePayment(order.getPayment());
        order.getItems().forEach(this::validateItems);

    }

    private void validateOrder(Order order){

    }

    private void validateItems(OrderItem orderItem){
        if(orderItem.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidFieldException("unitPrice","UnitPrice is 0 or smaller than 0.");
        }
    }

    private void validatePayment(Payment payment){

    }

    public void checkIdempotencyKey(String idempotencyKey){

    }
}
