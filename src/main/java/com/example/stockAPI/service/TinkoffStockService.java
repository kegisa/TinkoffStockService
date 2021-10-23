package com.example.stockAPI.service;

import com.example.stockAPI.model.Stock;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService{
    private final OpenApi api;

    public Stock getStockByTicker(String ticker){
        var stockList = api.getMarketContext().searchMarketInstrumentsByTicker(ticker).join();
        var list = stockList.getInstruments();
        if(list.isEmpty()) {
            throw new RuntimeException("not found");
        }

        var item =  list.get(0);
        return new Stock(item.getTicker(), item.getName(), item.getType().toString());
    }

}
