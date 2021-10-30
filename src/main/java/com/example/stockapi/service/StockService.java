package com.example.stockapi.service;

import com.example.stockapi.dto.FigiesDto;
import com.example.stockapi.dto.StockPrice;
import com.example.stockapi.dto.StocksPricesDto;
import com.example.stockapi.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
    StockPrice getPriceStockByFigi(String figi);
    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);
}
