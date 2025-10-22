package com.crypto.trading.service.impl;

import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.entity.PriceHistory;
import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.repository.PriceHistoryRepository;
import com.crypto.trading.service.PriceService;
import com.crypto.trading.util.BuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    private PriceAggregateRepository priceAggregateRepository;
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Override
    public Optional<PriceAggregate> getLatest(String symbol) {
        return priceAggregateRepository.findBySymbol(symbol);
    }

    /**
     * Saves the latest price record and logs it for later tracking.
     *
     * Note:
     * The log is very important because it ensures that
     * all price updates are stored and traceable for auditing
     * or historical data analysis purposes.
     *
     * @param ag the price data to persist and log
     */
    @Override
    @Transactional
    public void saveLastestPriceAndLog(PriceAggregate ag) {
        priceHistoryRepository.save(BuilderUtils.convertPriceAggregate2PriceHistory(ag));
        priceAggregateRepository.save(ag);
    }

    @Override
    public List<PriceHistory> getHistory(String symbol) {
        return priceHistoryRepository.findAllBySymbolOrderByCreatedAtDesc(symbol);
    }
}
