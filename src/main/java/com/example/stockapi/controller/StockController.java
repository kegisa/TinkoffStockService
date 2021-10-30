package com.example.stockapi.controller;

import com.example.stockapi.dto.FigiesDto;
import com.example.stockapi.dto.StockPrice;
import com.example.stockapi.dto.StocksPricesDto;
import com.example.stockapi.model.Stock;
import com.example.stockapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @GetMapping("prices/{figi}")
    public StockPrice getPriceStockByFigi(@PathVariable String figi) {
        return stockService.getPriceStockByFigi(figi);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPricesStocksByFigies(@RequestBody FigiesDto figiesDto) {
        return stockService.getPricesStocksByFigies(figiesDto);
    }
}
