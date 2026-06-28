package com.localizacao.ms_geo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-geo — Microserviço de Geolocalização")
                        .description("""
                                API responsável pelo armazenamento e consulta geoespacial de ocorrências de \
                                infraestrutura urbana. Integra-se com o ms-ocorrencias para registrar \
                                incidentes no mapa, oferece busca por raio, geração de mapa de calor e \
                                resolução de endereços via ViaCEP e OpenStreetMap Nominatim.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TCC — Infraestrutura Urbana")
                                .email("lucascamilo373@gmail.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8084").description("Desenvolvimento local")));
    }
}
