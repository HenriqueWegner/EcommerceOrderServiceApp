package io.github.henriquewegner.EcommerceOrderServiceApi.application.validator;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.CancellationStatusException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    public void validate(Order order){
        order.getItems().forEach(this::validateItems);
    }

    public void validateCancelling(Order order){
        validateOrderStatus(order.getStatus());

    }

    private void validateItems(OrderItem orderItem){
        validateUnitPrice(orderItem.getUnitPrice());
    }
    private void validateOrderStatus(OrderStatus status) {
        if(status.equals(OrderStatus.DELIVERED) || status.equals(OrderStatus.SHIPPED)){
            throw new CancellationStatusException("Order cannot be canceled because of it's status.");
        }
    }

    public void validateUnitPrice(BigDecimal unitPrice){
        if(unitPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidFieldException("unitPrice","UnitPrice is 0 or smaller than 0.");
        }
    }

}
