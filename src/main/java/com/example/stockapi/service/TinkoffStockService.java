package com.example.stockapi.service;

import com.example.stockapi.dto.FigiesDto;
import com.example.stockapi.dto.StockPrice;
import com.example.stockapi.dto.StocksPricesDto;
import com.example.stockapi.exception.StockNotFoundException;
import com.example.stockapi.model.Currency;
import com.example.stockapi.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        var orderBook = api.getMarketContext().getMarketOrderbook(figi,0);
        log.info("Getting {} from Tinkoff",figi);
        return orderBook;
    }

    public StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto) {
//        Long start = System.currentTimeMillis();
//        List<StockPrice> prices =  figiesDto.getFigies().stream()
//                .map(this::getPriceStockByFigi)
//                .collect(Collectors.toList());
//        log.info("time {}", Long.toString(System.currentTimeMillis() - start));
//        return new StocksPricesDto(prices);
        Long start = System.currentTimeMillis();
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        List<StockPrice> prices =  orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found.")))
                .map(orderBook -> new StockPrice(orderBook.getFigi(), orderBook.getLastPrice().doubleValue())).collect(Collectors.toList());

        log.info("time {}", System.currentTimeMillis() - start);

        return new StocksPricesDto(prices);
    }

}
