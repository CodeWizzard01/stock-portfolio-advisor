package com.codewiz.stockadvisor.controller;


import com.codewiz.stockadvisor.model.StockHolding;
import com.codewiz.stockadvisor.service.StockPositionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StockPositionController {

    private final StockPositionService stockPositionService;

    @GetMapping("/stock-position/{stockSymbol}")
    public StockHolding getStockPosition(@PathVariable String stockSymbol) {
        return stockPositionService.getStockPositionSequential(stockSymbol);
    }
}
