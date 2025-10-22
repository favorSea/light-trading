package com.crypto.trading.service.impl;

import com.crypto.trading.dto.PriceDataDto;
import com.crypto.trading.dto.PriceWitSource;
import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.provider.PriceProvider;
import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.service.PriceFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class PriceFetchServiceImpl implements PriceFetchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceFetchServiceImpl.class);

    @Autowired
    private WebClient webClient;

    @Autowired
    private PriceAggregateRepository priceRepo;

    @Autowired
    private Set<String> supportedSymbols;

    @Autowired
    private List<PriceProvider> priceProviders;

    @Scheduled(fixedDelay = 10_000)
    @Override
    public void fetchPrices() {
        try {
            List<PriceDataDto> priceDataDtoList = new LinkedList<>();
            for (PriceProvider priceProvider : priceProviders) {
               var priceDataDto = priceProvider.getLatestPrice();
               if (priceDataDto != null) {
                   priceDataDtoList.add(priceDataDto);
               }
            }

            for (String symbol : supportedSymbols) {
                var bestAskPrice = priceDataDtoList.stream().filter(p -> p.getAsks().get(symbol) != null)
                        .map(p -> new PriceWitSource(p.getAsks().get(symbol), p.getSource()))
                        .min(Comparator.comparing(PriceWitSource::price))
                        .get();

                var bestBidPrice = priceDataDtoList.stream().filter(p -> p.getBids().get(symbol) != null)
                        .map(p -> new PriceWitSource(p.getBids().get(symbol), p.getSource()))
                        .max(Comparator.comparing(PriceWitSource::price))
                        .get();

                BigDecimal bestAsk = bestAskPrice.price();
                String bestAskSource = bestAskPrice.source();
                BigDecimal bestBid = bestBidPrice.price();
                String bestBidSource = bestBidPrice.source();

                PriceAggregate ag = new PriceAggregate();
                ag.setSymbol(symbol);
                if (bestAsk != null) {
                    ag.setBestAsk(bestAsk);
                    ag.setBestAskSource(bestAskSource);
                }
                if (bestBid != null) {
                    ag.setBestBid(bestBid);
                    ag.setBestBidSource(bestBidSource);
                }
                ag.setUpdatedAt(Instant.now());
                priceRepo.save(ag);
            }

        } catch (Exception ex) {
            LOGGER.error("Price fetch error: {}", ex.getMessage(), ex);
        }
    }
}
