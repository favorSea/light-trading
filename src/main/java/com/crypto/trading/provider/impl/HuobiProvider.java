package com.crypto.trading.provider.impl;

import com.crypto.trading.config.CryptoTradingConfig;
import com.crypto.trading.constant.Constant;
import com.crypto.trading.dto.PriceDataDto;
import com.crypto.trading.dto.TradingPriceDto;
import com.crypto.trading.provider.PriceProvider;
import com.crypto.trading.util.FetchPriceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
public class HuobiProvider implements PriceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiProvider.class);

    @Autowired
    private WebClient webClient;
    @Autowired
    private Set<String> supportedSymbols;

    @Override
    public PriceDataDto getLatestPrice() {
        Mono<String> huobiMono = webClient.get()
                .uri(CryptoTradingConfig.getHuobiUrl())
                .retrieve()
                .bodyToMono(String.class);

        String huobiResp = huobiMono.block();

        TradingPriceDto huobiPrices;
        try {
            huobiPrices = FetchPriceUtils.parseHuobiPricesResponse(huobiResp, supportedSymbols, "ask",
                    "bid");
        } catch (Exception e) {
            LOGGER.warn("[HuobiProvider] got err", e);
            return null;
        }
        Map<String, BigDecimal> huobiAsk = huobiPrices.asks();
        Map<String, BigDecimal> huobiBid = huobiPrices.bids();

        return new PriceDataDto(huobiAsk, huobiBid, Constant.HUOBI_SOURCE);
    }
}
