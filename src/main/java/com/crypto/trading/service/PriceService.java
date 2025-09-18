package com.crypto.trading.service;

import com.crypto.trading.entity.PriceAggregate;

import java.util.Optional;

public interface PriceService {
    Optional<PriceAggregate> getLatest(String symbol);
}
