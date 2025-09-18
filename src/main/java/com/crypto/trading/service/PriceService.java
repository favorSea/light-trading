package com.crypto.trading.service;

import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.entity.PriceAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceService {
    @Autowired
    private PriceAggregateRepository repo;

    public Optional<PriceAggregate> getLatest(String symbol) {
        return repo.findBySymbol(symbol);
    }
}
