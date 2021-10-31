package com.example.stockapi.service;

import com.example.stockapi.dto.*;
import com.example.stockapi.exception.StockNotFoundException;
import com.example.stockapi.model.Currency;
import com.example.stockapi.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi api;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker) {
        log.info("Getting {} from Tinkoff", ticker);
        var context = api.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentTicker(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock % not found.", ticker));
        }
        var item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()));
    }

    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstruments.add(getMarketInstrumentTicker(ticker)));
        AtomicInteger i = new AtomicInteger();
        List<Stock> stocks =  marketInstruments.stream()
                .map(CompletableFuture::join)
                .map(mi -> {
                    if(mi.getInstruments().isEmpty()) {
                        throw new StockNotFoundException(
                                String.format(
                                        "Stock with ticker %s not found",
                                        tickers.getTickers().get(i.get())));
                    }
                    i.getAndIncrement();
                    return mi.getInstruments().get(0);
                })
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue())))
                .collect(Collectors.toList());

        return new StocksDto(stocks);
    }

    public StockPrice getPriceStockByFigi(String figi) {
        CompletableFuture<Optional<Orderbook>> cfOrderBook = getOrderBookByFigi(figi);
        var orderBook = cfOrderBook.join()
                .orElseThrow(() -> new StockNotFoundException("Stock not found.Try another figi."));
        return new StockPrice(orderBook.getFigi(), orderBook.getLastPrice().doubleValue());
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        var orderBook = api.getMarketContext().getMarketOrderbook(figi,0);
        log.info("Getting {} from Tinkoff",figi);
        return orderBook;
    }

    public StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto) {
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        List<StockPrice> prices =  orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found.")))
                .map(orderBook -> new StockPrice(orderBook.getFigi(), orderBook.getLastPrice().doubleValue())).collect(Collectors.toList());

        return new StocksPricesDto(prices);
    }

}
