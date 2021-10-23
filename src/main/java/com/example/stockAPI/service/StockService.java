package com.example.stockAPI.service;

import com.example.stockAPI.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
