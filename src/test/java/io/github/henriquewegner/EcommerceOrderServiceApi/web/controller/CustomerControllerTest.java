package io.github.henriquewegner.EcommerceOrderServiceApi.web.controller;

import io.github.henriquewegner.EcommerceOrderServiceApi.ports.in.usecase.CustomerUseCase;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.request.CustomerRequestDTO;
import io.github.henriquewegner.EcommerceOrderServiceApi.web.dto.response.CustomerOrdersResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerUseCase customerUseCase;
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        customerUseCase = mock(CustomerUseCase.class);
        controller = new CustomerController(customerUseCase);
    }


    @Test
    void save_returnsCreatedWithLocation() {
        UUID uuid = UUID.randomUUID();
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        when(customerUseCase.createCustomer(dto)).thenReturn(uuid);

        try (MockedStatic<ServletUriComponentsBuilder> mocked = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder builder = mock(ServletUriComponentsBuilder.class);
            UriComponents uriComponents = mock(UriComponents.class);

            mocked.when(ServletUriComponentsBuilder::fromCurrentRequest).thenReturn(builder);
            when(builder.path("/{id}")).thenReturn(builder);
            when(builder.buildAndExpand(uuid)).thenReturn(uriComponents);
            when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/customers/" + uuid));

            ResponseEntity<Void> response = controller.save(dto);

            assertEquals(201, response.getStatusCodeValue());
            assertNotNull(response.getHeaders().getLocation());
            assertTrue(response.getHeaders().getLocation().toString().contains(uuid.toString()));
        }
    }

    @Test
    void findCustomerOrders_found_returnsOk() {
        String id = "abc";
        CustomerOrdersResponseDTO orders = mock(CustomerOrdersResponseDTO.class);
        when(customerUseCase.findCustomerOrders(id)).thenReturn(Optional.of(orders));

        ResponseEntity<CustomerOrdersResponseDTO> response = controller.findCustomerOrders(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
    }

    @Test
    void findCustomerOrders_notFound_returnsNotFound() {
        String id = "abc";
        when(customerUseCase.findCustomerOrders(id)).thenReturn(Optional.empty());

        ResponseEntity<CustomerOrdersResponseDTO> response = controller.findCustomerOrders(id);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}