package com.riwi.microservice.coopcredit.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceCreditApplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCreditApplicationServiceApplication.class, args);
	}

}
