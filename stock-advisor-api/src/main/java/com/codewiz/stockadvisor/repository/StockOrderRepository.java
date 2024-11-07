package com.codewiz.stockadvisor.repository;

import com.codewiz.stockadvisor.model.StockOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface StockOrderRepository extends ListCrudRepository<StockOrder, Long> {
    List<StockOrder> findBySymbol(String symbol);
}
