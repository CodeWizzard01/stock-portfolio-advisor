package com.codewiz.stockadvisor.service;

import com.codewiz.stockadvisor.model.OrderType;
import com.codewiz.stockadvisor.model.StockHoldingDetails;
import com.codewiz.stockadvisor.model.StockOrder;
import com.codewiz.stockadvisor.repository.StockOrderRepository;
import dev.langchain4j.agent.tool.Tool;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockOrderService {

    private final StockOrderRepository stockOrderRepository;

    @Tool
    public StockOrder createOrder(StockOrder order) {
        StockOrder newOrder = new StockOrder(
                null,
                "testuser",
                order.symbol(),
                order.quantity(),
                order.price(),
                order.orderType(),
                LocalDateTime.now()
        );
        return stockOrderRepository.save(newOrder);
    }


    @Tool
    public List<StockOrder> getAllOrders() {
        return stockOrderRepository.findAll();
    }

    public StockOrder getOrderById(Long id) {
        return stockOrderRepository.findById(id).orElse(null);
    }

    public List<StockOrder> getOrdersBySymbol(String symbol) {
        return stockOrderRepository.findBySymbol(symbol);
    }

    @Tool
    public List<StockHoldingDetails> getStockHoldingDetails() {
        return stockOrderRepository.findAll().stream()
                .collect(Collectors.groupingBy(StockOrder::symbol, Collectors.summingInt(order ->
                        order.orderType() == OrderType.BUY ? order.quantity() : -order.quantity())))
                .entrySet().stream()
                .map(entry -> new StockHoldingDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
