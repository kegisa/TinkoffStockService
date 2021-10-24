package com.example.stockAPI.controller;

import com.example.stockAPI.model.Stock;
import com.example.stockAPI.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
class StockControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    StockService stockService;

    public static final String FXTEST = "FXTEST";
    public static final String NAME = "Finex test";
    public static final String TYPE = "Etf";
    Stock actualStock = new Stock(FXTEST, NAME, TYPE);

    @BeforeEach
    void beforeEach() {
        when(stockService.getStockByTicker(any())).thenReturn(actualStock);
    }

    @Test
    void getStock() throws Exception {
        var result = mockMvc.perform(get(String.format("/stocks/%s", FXTEST)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(stockService, times(1)).getStockByTicker(anyString());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(result, mapper.writeValueAsString(actualStock));
    }

}