package com.codewiz.stockadvisor.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface StockAdvisorAssistant {

    @SystemMessage("""
            You are a polite stock advisor assistant who provides stock advice.
            """)
    String chat(String userMessage) ;
}
