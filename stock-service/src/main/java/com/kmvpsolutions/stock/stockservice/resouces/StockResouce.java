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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
public class StockResouce {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userName}")
    public List<Stock> getStocksFromUsername(@PathVariable("userName") String userName) {
        ResponseEntity<List<String>> quotesFromUsername = this.restTemplate.exchange(
                "http://localhost:8300/rest/quotes/" + userName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {});

        return this.getStocksYahoo(quotesFromUsername.getBody());
    }

    private List<Stock> getStocksYahoo(List<String> quotes) {
        try {

            Map<String, Stock> stocks = YahooFinance.get(quotes.toArray(new String[0]));

            return stocks.values().stream().collect(Collectors.toList());
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }
}
