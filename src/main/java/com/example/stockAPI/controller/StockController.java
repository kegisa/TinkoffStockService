package com.example.stockAPI.controller;

import com.example.stockAPI.model.Stock;
import com.example.stockAPI.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockService stockService;


    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker) {
        return stockService.getStockByTicker(ticker);
    }
}
