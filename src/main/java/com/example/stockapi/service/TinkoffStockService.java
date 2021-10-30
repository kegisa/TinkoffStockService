package com.example.stockapi.service;

import com.example.stockapi.dto.FigiesDto;
import com.example.stockapi.dto.StockPrice;
import com.example.stockapi.dto.StocksPricesDto;
import com.example.stockapi.exception.StockNotFoundException;
import com.example.stockapi.model.Currency;
import com.example.stockapi.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi api;

    public Stock getStockByTicker(String ticker) {
        var context = api.getMarketContext();
        var stockList = context.searchMarketInstrumentsByTicker(ticker).join();
        var list = stockList.getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException("Stock not found.");
        }

        var item =  list.get(0);
        return new Stock(item.getTicker(), item.getFigi(), item.getName(), item.getType().toString(), Currency.valueOf(item.getCurrency().getValue()));
    }

    public StockPrice getPriceStockByFigi(String figi) {
        log.info("Getting {} from Tinkoff",figi);

        var orderBook = api.getMarketContext()
                .getMarketOrderbook(figi,0)
                .join().orElseThrow(() -> new StockNotFoundException("Stock not found.Try another figi."));

        return new StockPrice(orderBook.getFigi(), orderBook.getLastPrice().doubleValue());
    }

    public StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto) {
        List<StockPrice> stocks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> stocks.add(getPriceStockByFigi(figi)));
        return new StocksPricesDto(stocks);
    }

}
