package io.github.henriquewegner.EcommerceOrderServiceApi.application.services;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.RequestType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.OrderItem;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.ProductApi;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ProcessProductRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.ReservedItemRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockReservationService {
    private final ProductApi productApi;
    private final OrderItemMapper orderItemMapper;

    public StockReservationService(ProductApi productApi, OrderItemMapper orderItemMapper) {
        this.productApi = productApi;
        this.orderItemMapper = orderItemMapper;
    }

    public void processStock(Order order, RequestType requestType) {
        List<ReservedItemRequestDTO> reservedItems = getAndMapReservedItems(order.getItems());
        productApi.processStock(new ProcessProductRequestDTO(requestType, reservedItems));
    }

    public void reserveStock(Order order) {
        processStock(order, RequestType.RESERVE);
    }

    public void restockProduct(Order order) {
        processStock(order, RequestType.RESTOCK);
    }

    public void removeFromStock(Order order){
        processStock(order, RequestType.REMOVE);
    }

    public List<ReservedItemRequestDTO> getAndMapReservedItems(List<OrderItem> orderItems){
        return orderItemMapper.toReservedItemRequestDTOs(orderItems);

    }
}