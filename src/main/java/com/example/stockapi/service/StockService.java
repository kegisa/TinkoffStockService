package com.example.stockapi.service;

import com.example.stockapi.dto.*;
import com.example.stockapi.model.Stock;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.concurrent.CompletableFuture;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StockPrice getPriceStockByFigi(String figi);

    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);

    StocksDto getStocksByTickers(TickersDto tickers);
}
