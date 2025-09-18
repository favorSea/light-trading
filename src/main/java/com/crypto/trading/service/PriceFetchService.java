package com.crypto.trading.service;

import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.repository.PriceAggregateRepository;
import com.crypto.trading.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceFetchService {
    private static final String BINANCE_SOURCE = "binance";
    private static final String HUOBI_SOURCE = "huobi";

    @Autowired
    private WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PriceAggregateRepository priceRepo;

    private final List<String> symbols = Arrays.asList("ETHUSDT", "BTCUSDT");

    @Scheduled(fixedDelay = 10000)
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

            Map<String, Double> binAsk = new HashMap<>();
            Map<String, Double> binBid = new HashMap<>();

            JsonNode binRoot = mapper.readTree(binResp);
            if (binRoot.isArray()) {
                for (JsonNode n : binRoot) {
                    String symbol = n.path("symbol").asText();
                    if (symbols.contains(symbol)) {
                        double ask = n.path("askPrice").asDouble();
                        double bid = n.path("bidPrice").asDouble();
                        binAsk.put(symbol, ask);
                        binBid.put(symbol, bid);
                    }
                }
            }

            Map<String, Double> huobiAsk = new HashMap<>();
            Map<String, Double> huobiBid = new HashMap<>();

            JsonNode huRoot = mapper.readTree(huobiResp);
            JsonNode huData = huRoot.path("data");
            if (huData.isArray()) {
                for (JsonNode n : huData) {
                    String symbol = n.path("symbol").asText().toUpperCase();
                    if (symbols.contains(symbol)) {
                        double bid = n.path("bid").asDouble();
                        double ask = n.path("ask").asDouble();
                        huobiAsk.put(symbol, ask);
                        huobiBid.put(symbol, bid);
                    }
                }
            }

            for (String symbol : symbols) {
                Double bAsk = Utils.getOrDefault(binAsk.get(symbol), Double.MAX_VALUE);
                Double hAsk = Utils.getOrDefault(huobiAsk.get(symbol), Double.MAX_VALUE);
                Double bBid = Utils.getOrDefault(binBid.get(symbol), Double.MIN_VALUE);
                Double hBid = Utils.getOrDefault(huobiBid.get(symbol), Double.MIN_VALUE);

                Double bestAsk;
                String bestAskSource;
                Double bestBid;
                String bestBidSource;

                if (bAsk <= hAsk) {
                    bestAsk = bAsk;
                    bestAskSource = BINANCE_SOURCE;
                } else {
                    bestAsk = hAsk;
                    bestAskSource = HUOBI_SOURCE;
                }

                if (bBid >= hBid) {
                    bestBid = bBid;
                    bestBidSource = BINANCE_SOURCE;
                } else {
                    bestBid = hBid;
                    bestBidSource = HUOBI_SOURCE;
                }


                PriceAggregate ag = priceRepo.findBySymbol(symbol).orElseGet(() -> {
                    PriceAggregate p = new PriceAggregate();
                    p.setSymbol(symbol);
                    return p;
                });
                ag.setBestAsk(bestAsk);
                ag.setBestAskSource(bestAskSource);
                ag.setBestBid(bestBid);
                ag.setBestBidSource(bestBidSource);
                ag.setUpdatedAt(Instant.now());
                priceRepo.save(ag);
            }

        } catch (
                Exception ex) {
            System.err.println("Price fetch error: " + ex.getMessage());
        }
    }
}
