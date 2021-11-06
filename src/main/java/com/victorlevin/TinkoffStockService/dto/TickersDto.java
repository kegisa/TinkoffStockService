package com.victorlevin.TinkoffStockService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TickersDto {
    private List<String> tickers;
}
