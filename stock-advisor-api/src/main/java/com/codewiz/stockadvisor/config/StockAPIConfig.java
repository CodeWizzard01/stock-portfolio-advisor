package com.codewiz.stockadvisor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class StockAPIConfig {

    @Bean
    public RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl("https://financialmodelingprep.com/api/v3")
                .build();
    }

    private @Value("${stock.api.key}") String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
