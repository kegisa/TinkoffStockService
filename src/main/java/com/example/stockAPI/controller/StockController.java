package com.example.stockAPI.controller;

import com.example.stockAPI.model.Stock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker) {
        return null;
    }
}
