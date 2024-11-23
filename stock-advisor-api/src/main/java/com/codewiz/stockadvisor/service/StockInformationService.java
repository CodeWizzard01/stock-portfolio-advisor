package com.codewiz.stockadvisor.service;

import com.codewiz.stockadvisor.config.StockAPIConfig;
import com.codewiz.stockadvisor.model.CompanyDetails;
import com.codewiz.stockadvisor.model.StockPrice;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class StockInformationService {

    private final StockAPIConfig stockAPIConfig;
    private final RestClient restClient;

    @Tool("Returns the stock price for the given stock symbols")
    public String getStockPrice(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching stock price for stock symbols: {}", stockSymbols);
        return fetchData("/quote/" + stockSymbols);
    }

    @Tool("Returns the company profile for the given stock symbols")
    public String getCompanyProfile(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching company profile for stock symbols: {}", stockSymbols);
        return fetchData("/profile/" + stockSymbols);
    }

    @Tool("Returns the balance sheet statements for the given stock symbols")
    public List<String> getBalanceSheetStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching balance sheet statements for stock symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/balance-sheet-statement/");
    }

    @Tool("Returns the income statements for the given stock symbols")
    public List<String> getIncomeStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching income statements for stock symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/income-statement/");
    }

    @Tool("Returns the cash flow statements for the given stock symbols")
    public List<String> getCashFlowStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching cash flow statements for stock symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/cash-flow-statement/");
    }

    private List<String> fetchDataForMultipleSymbols(String stockSymbols, String s) {
        List<String> data = new ArrayList<>();
        for (String symbol : stockSymbols.split(",")) {
            String response = fetchData(s + symbol);
            data.add(response);
        }
        return data;
    }

    private String fetchData(String s) {
        return restClient.get()
                .uri(s + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(String.class)
                .replaceAll("\\s+", " ").trim();
    }

    public StockPrice getPrice(String symbol) {
        log.info("getPrice.start.Current thread: "+Thread.currentThread());
        var priceList = restClient.get()
                .uri("/quote/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockPrice>>() {
                });
        if(priceList==null || priceList.isEmpty()){
            throw new RuntimeException("No price found for the symbol: "+symbol);
        }
        log.info("getPrice.end.Current thread: "+Thread.currentThread());
        return priceList.getFirst();
    }

    public CompanyDetails getCompanyDetails(String symbol) {
        log.info("getCompanyDetails.start.Current thread: "+Thread.currentThread());
        var companyList = restClient.get()
                .uri("/profile/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CompanyDetails>>() {
                });
        if(companyList==null || companyList.isEmpty()){
            throw new RuntimeException("No company details found for the symbol: "+symbol);
        }
        log.info("getCompanyDetails.end.Current thread: "+Thread.currentThread());
        return companyList.getFirst();
    }

    @Async
    public CompletableFuture<StockPrice> getPriceAsync(String symbol) {
        log.info("getPriceAsync.start.Current thread: " + Thread.currentThread());
        var priceList = restClient.get()
                .uri("/quote/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockPrice>>() {
                });
        if (priceList == null || priceList.isEmpty()) {
            throw new RuntimeException("No price found for the symbol: " + symbol);
        }
        log.info("getPriceAsync.end.Current thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(priceList.getFirst());
    }

    @Async
    public CompletableFuture<CompanyDetails> getCompanyDetailsAsync(String symbol) {
        log.info("getCompanyDetailsAsync.start.Current thread: " + Thread.currentThread());
        var companyList = restClient.get()
                .uri("/profile/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CompanyDetails>>() {
                });
        if (companyList == null || companyList.isEmpty()) {
            throw new RuntimeException("No company details found for the symbol: " + symbol);
        }
        log.info("getCompanyDetailsAsync.end.Current thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(companyList.getFirst());
    }

}
