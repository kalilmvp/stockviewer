package com.kmvpsolutions.stock.databaseservice.repositories;

import com.kmvpsolutions.stock.databaseservice.repositories.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    List<Quote> findByUserName(String username);
}
