package com.victorlevin.TinkoffStockService.service;

import com.victorlevin.TinkoffStockService.dto.*;
import com.victorlevin.TinkoffStockService.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);

    StocksDto getStocksByTickers(TickersDto tickers);
}
