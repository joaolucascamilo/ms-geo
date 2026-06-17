package com.localizacao.ms_geo.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor nominatimUserAgentInterceptor() {
        return template -> {
            if (template.feignTarget().name().equals("nominatim")) {
                template.header("User-Agent", "ms-geo-tcc/1.0");
            }
        };
    }
}
