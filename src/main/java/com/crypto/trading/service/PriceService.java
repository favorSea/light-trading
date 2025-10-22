package com.crypto.trading.service;

import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.entity.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceService {
    Optional<PriceAggregate> getLatest(String symbol);

    void saveLastestPriceAndLog(PriceAggregate ag);

    List<PriceHistory> getHistory(String symbol);
}
