package com.localizacao.ms_geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsGeoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGeoApplication.class, args);
	}

}
