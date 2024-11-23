package com.codewiz.stockadvisor.controller;

import com.codewiz.stockadvisor.assistant.StockAdvisorAssistant;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StockAdvisorController {

    private final StockAdvisorAssistant assistant;

    @GetMapping("/chat")
    public String chat(String userMessage) {
        return assistant.chat(userMessage);
    }
}
