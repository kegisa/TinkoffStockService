package com.victorlevin.TinkoffStockService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class StockApiApplication {
   public static void main(String[] args) {
     SpringApplication.run(StockApiApplication.class, args);
   }
}
