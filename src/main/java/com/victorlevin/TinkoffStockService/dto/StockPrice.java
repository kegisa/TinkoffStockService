package com.victorlevin.TinkoffStockService.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class StockPrice {
    private String figi;
    private Double price;
}
