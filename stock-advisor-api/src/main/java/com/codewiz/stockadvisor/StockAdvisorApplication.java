package com.codewiz.stockadvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StockAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAdvisorApplication.class, args);
	}

}
