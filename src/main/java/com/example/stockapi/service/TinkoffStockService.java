package com.example.stockapi.service;

import com.example.stockapi.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;
import ru.tinkoff.invest.openapi.model.rest.UserAccount;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi api;

    public List<PortfolioPosition> getPortfolioPositions() throws ExecutionException, InterruptedException {
        UserAccount userAccount = api.getUserContext().getAccounts().get().getAccounts().stream().findFirst().get();
        return api.getPortfolioContext()
                .getPortfolio(userAccount.getBrokerAccountId()).get().getPositions();
    }

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
