package com.riwi.microservice.coopcredit.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceRiskCentralServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRiskCentralServiceApplication.class, args);
	}

}
