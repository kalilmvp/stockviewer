package com.kmvpsolutions.stock.databaseservice.resource;

import com.kmvpsolutions.stock.databaseservice.model.Quotes;
import com.kmvpsolutions.stock.databaseservice.repositories.QuoteRepository;
import com.kmvpsolutions.stock.databaseservice.repositories.entities.Quote;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/quotes")
public class QuoteResource {
    private QuoteRepository quoteRepository;

    public QuoteResource(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") String username) {
        return this.getQuotesByUsername(username);
    }

    private List<String> getQuotesByUsername(String username) {
        return this.quoteRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes) {
        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUserName(), quote))
                .forEach(quote -> this.quoteRepository.save(quote));

        return this.getQuotesByUsername(quotes.getUserName());
    }

    @DeleteMapping("/delete/{username}")
    public List<String> delete(@PathVariable("username") String username) {
        this.quoteRepository.delete(this.quoteRepository.findByUserName(username));
        return getQuotesByUsername(username);
    }
}
