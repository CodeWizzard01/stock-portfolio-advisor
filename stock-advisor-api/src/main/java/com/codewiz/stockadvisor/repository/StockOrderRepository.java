package com.codewiz.stockadvisor.repository;

import com.codewiz.stockadvisor.model.StockOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockOrderRepository extends ListCrudRepository<StockOrder, Long> {
    List<StockOrder> findBySymbol(String symbol);
}
