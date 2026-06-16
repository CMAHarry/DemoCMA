package com.cma.systemc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI systemCOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("System C Manufacturing Demo API")
                        .description("REST API for ISM 2 integration with System C manufacturing work orders, products, and raw materials.")
                        .version("1.0.0")
                        .contact(new Contact().name("CMA Solutions"))
                        .license(new License().name("Demo Use")));
    }
}
