package io.github.henriquewegner.EcommerceOrderServiceApi.application.usecase;

import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.AddressEnrichmentService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.OutboxEventService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.ShippingQuotationService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.services.StockReservationService;
import io.github.henriquewegner.EcommerceOrderServiceApi.application.validator.OrderValidator;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.EventType;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.OrderStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.enums.PaymentStatus;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.model.Order;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.persistence.entities.OrderIdempotencyEntity;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.eventhandler.PaymentEventHandler;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.OrderUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.api.CustomerApi;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderIdempotencyRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.ports.out.repository.OrderRepository;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.DuplicatedRegistryException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.OrderRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.PaymentUpdateRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CreatedOrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.OrderResponseDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderIdempotencyMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.OrderMapper;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.hash;

@Service
@RequiredArgsConstructor
public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderIdempotencyRepository idempotencyRepository;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final OrderIdempotencyMapper orderIdempotencyMapper;
    private final OrderValidator orderValidator;
    private final OutboxEventService outboxEventService;
    private final StockReservationService stockReservationService;
    private final AddressEnrichmentService addressEnrichmentService;
    private final ShippingQuotationService shippingQuotationService;
    private final CustomerApi customerApi;
    private final PaymentEventHandler paymentEventHandler;


    @Override
    @Transactional
    public CreatedOrderResponseDTO createOrder(OrderRequestDTO orderDTO) {
        String requestHash = String.valueOf(hash(orderDTO));
        Optional<CreatedOrderResponseDTO> alreadyExistsResponse = checkIdempotency(orderDTO, requestHash);

        if (alreadyExistsResponse.isPresent()) {
            return alreadyExistsResponse.get();
        }

        checkIfCustomerExists(orderDTO.customerId());
        Order order = prepareOrder(orderDTO);
        OrderEntity savedEntity = orderRepository.save(order);
        saveOutboxOrderEvent(savedEntity);
        saveOutboxPaymentEvent(savedEntity);

        CreatedOrderResponseDTO response = orderMapper.orderEntityToCreatedOrderResponseDTO(savedEntity);
        saveIdempotency(orderDTO, response, requestHash);

        return response;
    }


    @Override
    public Optional<OrderResponseDTO> findOrder(String id) {

        return orderRepository.findById(UUID.fromString(id))
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderResponseDTO> findOrdersByCustomer(String customerId) {
        List<OrderEntity> customerList = orderRepository.findByCustomerId(UUID.fromString(customerId));
        return orderMapper.entityListToDtoList(customerList);
    }

    @Override
    @Transactional
    public Optional<OrderResponseDTO> updatePayment(String id, PaymentUpdateRequestDTO paymentUpdateRequestDTO) {
        Optional<OrderEntity> savedEntity = paymentEventHandler.handlePaymentUpdate(
                UUID.fromString(id), paymentUpdateRequestDTO.status(), paymentUpdateRequestDTO.cardToken());

        return savedEntity.map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<OrderResponseDTO> cancelOrder(String id) {

        return orderRepository.findById(UUID.fromString(id))
                .map(orderEntity -> {

                    Order order = orderMapper.toDomain(orderEntity);
                    orderValidator.validateCancelling(order);
                    order.setStatus(OrderStatus.CANCELLED);
                    OrderEntity savedEntity = orderRepository.save(order);
                    saveOutboxOrderEvent(savedEntity);

                    return Optional.of(savedEntity).map(orderMapper::toDto);
                }).orElse(Optional.empty());
    }

    private Optional<CreatedOrderResponseDTO> checkIdempotency(OrderRequestDTO orderDTO, String requestHash) {
        Optional<OrderIdempotencyEntity> existing = idempotencyRepository
                .findByIdCustomerIdAndIdIdempotencyKey(
                        UUID.fromString(orderDTO.customerId()),
                        orderDTO.idempotencyKey());

        if (existing.isPresent()) {
            if (!existing.get().getRequestHash().equals(requestHash)) {
                throw new DuplicatedRegistryException("Request with the same idempotencyKey but different payload!");
            }

            CreatedOrderResponseDTO response = orderIdempotencyMapper.toDTO(existing.get().getResponse());

            return Optional.of(response);
        }

        return Optional.empty();

    }

    private void checkIfCustomerExists(String customerId) {
        Optional.ofNullable(customerApi.findCustomer(customerId))
                .orElseThrow(() -> new ExternalApiException("Customer not found."));
    }

    private Order prepareOrder(OrderRequestDTO orderDTO) {
        Order order = orderMapper.toDomain(orderDTO);
        orderValidator.validate(order);
        setInitialStatus(order);
        stockReservationService.reserveStock(order);
        addressEnrichmentService.enrichAddress(order);
        shippingQuotationService.quoteShipping(order);

        return order;
    }

    private void setInitialStatus(Order order) {
        order.getPayment().setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.CREATED);
    }

    private void saveOutboxOrderEvent(OrderEntity orderEntity) {
        outboxEventService.saveOutboxEvent(orderEntity, EventType.ORDER_EVENT, orderMapper.toEvent(orderEntity));
    }

    private void saveOutboxPaymentEvent(OrderEntity orderEntity) {
        outboxEventService.saveOutboxEvent(orderEntity, EventType.PAYMENT_EVENT, paymentMapper.toEvent(orderEntity.getPayment()));
    }

    private void saveIdempotency(OrderRequestDTO dto, CreatedOrderResponseDTO response, String hash) {
        String stringResponse = orderIdempotencyMapper.toJson(response);
        OrderIdempotencyEntity entity = orderIdempotencyMapper.toEntity(dto);
        entity.setRequestHash(hash);
        entity.setResponse(stringResponse);

        idempotencyRepository.save(entity);
    }
}
