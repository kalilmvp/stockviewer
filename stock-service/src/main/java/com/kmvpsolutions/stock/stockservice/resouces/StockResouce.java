package com.kmvpsolutions.stock.stockservice.resouces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
public class StockResouce {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userName}")
    public List<Quote> getStocksFromUsername(@PathVariable("userName") String userName) {
        ResponseEntity<List<String>> quotesFromUsername = this.restTemplate.exchange(
                "http://database-service/rest/quotes/" + userName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {});

        return this.getStocksYahoo(quotesFromUsername.getBody());
    }

    private List<Quote> getStocksYahoo(List<String> quotes) {
        try {

            Map<String, Stock> stocks = YahooFinance.get(quotes.toArray(new String[0]));

            return stocks.values().stream()
                    .map(stock -> new Quote(stock.getName(), stock.getQuote().getSymbol(), stock.getQuote().getPrice()))
                    .collect(Collectors.toList());
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }

    private class Quote {
        private final String name;
        private final String quote;
        private final BigDecimal price;

        public Quote(String name, String quote, BigDecimal price) {
            this.name = name;
            this.quote = quote;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getQuote() {
            return quote;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
