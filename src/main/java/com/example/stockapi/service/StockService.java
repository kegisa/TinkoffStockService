package com.example.stockapi.service;

import com.example.stockapi.dto.*;
import com.example.stockapi.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StockPrice getPriceStockByFigi(String figi);

    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);

    StocksDto getStocksByTickers(TickersDto tickers);
}
