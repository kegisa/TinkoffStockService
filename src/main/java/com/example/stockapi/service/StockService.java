package com.example.stockapi.service;

import com.example.stockapi.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
