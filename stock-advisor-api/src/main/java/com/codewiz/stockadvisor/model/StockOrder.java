package com.codewiz.stockadvisor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("stock_orders")
public record StockOrder(
        @Id Long id,
        @Column("user_id")
        String userId,
        String symbol,
        Integer quantity,
        BigDecimal price,
        @Column("order_type")
        OrderType orderType,
        LocalDateTime createdAt
) {}

