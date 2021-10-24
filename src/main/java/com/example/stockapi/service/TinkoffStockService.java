package com.example.stockapi.service;

import com.example.stockapi.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi api;

    public Stock getStockByTicker(String ticker) {
        var context = api.getMarketContext();
        var stockList = context.searchMarketInstrumentsByTicker(ticker).join();
        var list = stockList.getInstruments();
        if (list.isEmpty()) {
            throw new RuntimeException("not found");
        }

        var item =  list.get(0);
        return new Stock(item.getTicker(), item.getName(), item.getType().toString());
    }

}
