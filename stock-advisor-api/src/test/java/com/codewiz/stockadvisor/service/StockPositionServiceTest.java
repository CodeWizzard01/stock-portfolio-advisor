package com.codewiz.stockadvisor.service;

import com.codewiz.stockadvisor.model.StockHolding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StockPositionServiceTest {

    @Autowired
    private StockPositionService stockPositionService;

  @Test
    void getStockPositionSequential() {
        String symbol = "AAPL";
        StockHolding stockHolding = stockPositionService.getStockPositionSequential(symbol);
        assertNotNull(stockHolding);
        assertEquals(symbol, stockHolding.stockSymbol());
    }

    @Test
    void getStockPositionCF() {
        String symbol = "AAPL";
        StockHolding stockHolding = stockPositionService.getStockPositionUsingCF(symbol);
        assertNotNull(stockHolding);
        assertEquals(symbol, stockHolding.stockSymbol());
    }

    @Test
    void getStockPositionAsync() {
        String symbol = "AAPL";
        StockHolding stockHolding = stockPositionService.getStockPositionUsingAsync(symbol);
        assertNotNull(stockHolding);
        assertEquals(symbol, stockHolding.stockSymbol());
    }
}