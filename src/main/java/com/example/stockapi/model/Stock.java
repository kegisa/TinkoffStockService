package com.example.stockapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Stock {
    String ticker;
    String name;
    String type;

    @JsonCreator
    public Stock(@JsonProperty("ticker") String ticker,
                        @JsonProperty("name") String name,
                        @JsonProperty("type") String type) {
        this.ticker = ticker;
        this.name = name;
        this.type = type;
    }
}
