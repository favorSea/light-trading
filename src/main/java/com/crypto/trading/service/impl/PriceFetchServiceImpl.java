package com.crypto.trading.service.impl;

import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.service.PriceFetchService;
import com.crypto.trading.util.FetchPriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Service
public class PriceFetchServiceImpl implements PriceFetchService {
    private static final String BINANCE_SOURCE = "binance";
    private static final String HUOBI_SOURCE = "huobi";

    @Autowired
    private WebClient webClient;

    @Autowired
    private PriceAggregateRepository priceRepo;

    @Autowired
    private Set<String> supportedSymbols;

    @Scheduled(fixedDelay = 10_000)
    @Override
    public void fetchPrices() {
        try {
            Mono<String> binanceMono = webClient.get()
                    .uri("https://api.binance.com/api/v3/ticker/bookTicker")
                    .retrieve()
                    .bodyToMono(String.class);

            Mono<String> huobiMono = webClient.get()
                    .uri("https://api.huobi.pro/market/tickers")
                    .retrieve()
                    .bodyToMono(String.class);

            String binResp = binanceMono.block();
            String huobiResp = huobiMono.block();


            var binancePrices = FetchPriceUtils.parsePricesResponse(binResp, supportedSymbols, "askPrice",
                    "bidPrice");
            Map<String, BigDecimal> binAsk = binancePrices.asks();
            Map<String, BigDecimal> binBid = binancePrices.bids();


            var huobiPrices = FetchPriceUtils.parsePricesResponse(huobiResp, supportedSymbols, "ask",
                    "bid");
            Map<String, BigDecimal> huobiAsk = huobiPrices.asks();
            Map<String, BigDecimal> huobiBid = huobiPrices.bids();

            for (String symbol : supportedSymbols) {
                BigDecimal bBid = binBid.get(symbol);
                BigDecimal hBid = huobiBid.get(symbol);

                BigDecimal bestAsk = null;
                String bestAskSource = null;
                BigDecimal bestBid = null;
                String bestBidSource = null;

                BigDecimal bAsk = binAsk.get(symbol);
                BigDecimal hAsk = huobiAsk.get(symbol);
                if (bAsk != null && hAsk != null) {
                    if (bAsk.compareTo(hAsk) <= 0) {
                        bestAsk = bAsk;
                        bestAskSource = BINANCE_SOURCE;
                    } else {
                        bestAsk = hAsk;
                        bestAskSource = HUOBI_SOURCE;
                    }
                } else if (bAsk != null) {
                    bestAsk = bAsk;
                    bestAskSource = BINANCE_SOURCE;
                } else if (hAsk != null) {
                    bestAsk = hAsk;
                    bestAskSource = HUOBI_SOURCE;
                }

                if (bBid != null && hBid != null) {
                    if (bBid.compareTo(hBid) >= 0) {
                        bestBid = bBid;
                        bestBidSource = BINANCE_SOURCE;
                    } else {
                        bestBid = hBid;
                        bestBidSource = HUOBI_SOURCE;
                    }

                } else if (bBid != null) {
                    bestBid = bBid;
                    bestBidSource = BINANCE_SOURCE;
                } else if (hBid != null) {
                    bestBid = hBid;
                    bestBidSource = HUOBI_SOURCE;
                }

                PriceAggregate ag = priceRepo.findBySymbol(symbol).orElseGet(() -> {
                    PriceAggregate p = new PriceAggregate();
                    p.setSymbol(symbol);
                    return p;
                });
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
            System.err.println("Price fetch error: " + ex);
        }
    }
}
