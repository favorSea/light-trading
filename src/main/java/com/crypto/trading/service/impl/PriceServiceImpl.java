package com.crypto.trading.service.impl;

import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    private PriceAggregateRepository repo;

    @Override
    public Optional<PriceAggregate> getLatest(String symbol) {
        return repo.findBySymbol(symbol);
    }
}
