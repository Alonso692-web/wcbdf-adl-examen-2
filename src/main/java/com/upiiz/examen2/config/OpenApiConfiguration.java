package com.upiiz.examen2.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Expenses",
                description = "Esta API proporciona acceso a un CRUD de Expenses",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alonso Dominguez Lopez",
                        email = "adominguezl2100@alumno.ipn.mx",
                        url = "http://localhost:8081/contacto"
                ),
                license = @License(),
                termsOfService = "Este programa es publico ajeno a cualquier partido político"
        ),
        servers = {
                @Server(
                        description = "Servidor de pruebas",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "Servidor de Produccion",
                        url = "https://wcbdf-adl-examen-2.onrender.com"
                )
        },
        tags = {
                @Tag(
                        name = "Expenses",
                        description = "Endpoints para expenses"
                )
        }
)
public class OpenApiConfiguration {

}
