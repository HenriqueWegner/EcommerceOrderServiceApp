package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ecommerce Order Service API",
                version = "V1",
                contact = @Contact(
                        name = "Henrique Wegner",
                        email = "henriqueawegner@gmail.com"
                )
        )
)
public class OpenApiConfiguration {
}
