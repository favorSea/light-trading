package com.crypto.trading.provider.impl;

import com.crypto.trading.config.CryptoTradingConfig;
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

import static com.crypto.trading.constant.Constant.BINANCE_SOURCE;

@Service
public class BinanceProvider implements PriceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinanceProvider.class);

    @Autowired
    private WebClient webClient;
    @Autowired
    private Set<String> supportedSymbols;

    @Override
    public PriceDataDto getLatestPrice() {
        Mono<String> binanceMono = webClient.get()
                .uri(CryptoTradingConfig.getBinanceUrl())
                .retrieve()
                .bodyToMono(String.class);

        String binResp = binanceMono.block();

        TradingPriceDto binancePrices;
        try {
            binancePrices = FetchPriceUtils.parseBinancePricesResponse(binResp, supportedSymbols, "askPrice",
                    "bidPrice");
        } catch (Exception e) {
            LOGGER.error("[BinanceProvider] err {}", e.getMessage(), e);
            return null;
        }
        Map<String, BigDecimal> binAsk = binancePrices.asks();
        Map<String, BigDecimal> binBid = binancePrices.bids();

        return new PriceDataDto(binAsk, binBid, BINANCE_SOURCE);
    }
}
