package com.codewiz.stockadvisor.service;


import com.codewiz.stockadvisor.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;

@Service
@AllArgsConstructor
@Slf4j
public class StockPositionService {

    private final StockOrderService stockOrderService;
    private final StockInformationService stockInformationService;

    public StockHolding getStockPositionSequential(String symbol) {
        long startTime = System.currentTimeMillis();

        List<StockOrder> orders = stockOrderService.getOrdersBySymbol(symbol); // Gets data from database
        var price = stockInformationService.getPrice(symbol); // Gets market price from external service
        var company = stockInformationService.getCompanyDetails(symbol); // Gets company details from external service

        log.info("getStockPositionSequential.Time taken to get stock position: {}ms", System.currentTimeMillis() - startTime);

        return getStockHolding(orders, price, company);
    }


    /*public StockHolding  getStockPositionUsingCF(String symbol) {
        long startTime = System.currentTimeMillis();

        CompletableFuture<List<StockOrder>> ordersFuture = CompletableFuture.supplyAsync(() -> stockOrderService.getOrdersBySymbol(symbol));
        CompletableFuture<StockPrice> priceFuture = CompletableFuture.supplyAsync(() -> stockInformationService.getPrice(symbol));
        CompletableFuture<CompanyDetails> companyFuture = CompletableFuture.supplyAsync(() -> stockInformationService.getCompanyDetails(symbol));

        try {
            List<StockOrder> orders = ordersFuture.get();
            StockPrice price = priceFuture.get();
            CompanyDetails company = companyFuture.get();

            log.info("getStockPositionUsingCF.Time taken to get stock position: {}ms", System.currentTimeMillis() - startTime);

            return getStockHolding(orders, price, company);
        } catch (Exception e) {
            log.error("Error while getting stock position", e);
            return null;
        }
    }*/


    public StockHolding  getStockPositionUsingCF(String symbol) {
        long startTime = System.currentTimeMillis();

        CompletableFuture<List<StockOrder>> ordersFuture = CompletableFuture.supplyAsync(() -> stockOrderService.getOrdersBySymbol(symbol));
        CompletableFuture<StockPrice> priceFuture = CompletableFuture.supplyAsync(() -> stockInformationService.getPrice(symbol));
        CompletableFuture<CompanyDetails> companyFuture = CompletableFuture.supplyAsync(() -> stockInformationService.getCompanyDetails(symbol));

        return ordersFuture.thenCombine(priceFuture, OrdersAndPrice::new)
                .thenCombine(companyFuture, (ordersAndPrice, company) -> {
                    log.info("getStockPositionUsingCF.Time taken to get stock position: {}ms", System.currentTimeMillis() - startTime);
                    return getStockHolding(ordersAndPrice.orders, ordersAndPrice.price, company);
                })
                .exceptionally(e -> {
                    log.error("Error while getting stock position", e);
                    return null;
                })
                .join();
    }

    public StockHolding  getStockPositionUsingAsync(String symbol) {
        long startTime = System.currentTimeMillis();

        CompletableFuture<List<StockOrder>> ordersFuture = stockOrderService.getOrdersBySymbolAsync(symbol);
        CompletableFuture<StockPrice> priceFuture = stockInformationService.getPriceAsync(symbol);
        CompletableFuture<CompanyDetails> companyFuture = stockInformationService.getCompanyDetailsAsync(symbol);

        return ordersFuture.thenCombine(priceFuture, OrdersAndPrice::new)
                .thenCombine(companyFuture, (ordersAndPrice, company) -> {
                    log.info("getStockPositionUsingAsync.Time taken to get stock position: {}ms", System.currentTimeMillis() - startTime);
                    return getStockHolding(ordersAndPrice.orders, ordersAndPrice.price, company);
                })
                .exceptionally(e -> {
                    log.error("Error while getting stock position", e);
                    return null;
                })
                .join();
    }

    public StockHolding getStockPositionStructuredConcurreny(String symbol) {
        long startTime = System.currentTimeMillis();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var ordersTask = scope.fork(() -> stockOrderService.getOrdersBySymbol(symbol));
            var priceTask = scope.fork(() -> stockInformationService.getPrice(symbol));
            var companyDetailsTask = scope.fork(() -> stockInformationService.getCompanyDetails(symbol));
            scope.join();
            scope.throwIfFailed();
            List<StockOrder> orders = ordersTask.get();
            StockPrice price = priceTask.get();
            CompanyDetails company = companyDetailsTask.get();
            log.info("getStockPositionStructuredConcurreny.Time taken to get stock position: {}ms", System.currentTimeMillis() - startTime);
            return getStockHolding(orders, price, company);
        } catch (Exception e) {
            log.error("Error while getting stock position", e);
            return null;
        }
    }



    private StockHolding getStockHolding(List<StockOrder> orders, StockPrice price, CompanyDetails company) {
        double totalQuantity = orders.stream()
                .mapToDouble(order -> order.orderType() == OrderType.BUY ? order.quantity() : -order.quantity())
                .sum();
        return buildStockHolding(orders.getFirst().symbol(), totalQuantity, price.price(), company);
    }

    private StockHolding buildStockHolding(String symbol, Double quantity, Double marketPrice, CompanyDetails company) {
        return new StockHolding(
                symbol,
                quantity,
                quantity * marketPrice,
                marketPrice,
                company.companyName(),
                company.industry(),
                company.website(),
                company.sector()
        );
    }

    record OrdersAndPrice(List<StockOrder> orders, StockPrice price) {
    }


}
