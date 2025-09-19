package com.crypto.trading.config;

import com.crypto.trading.constant.Constant;
import com.crypto.trading.constant.Currency;
import com.crypto.trading.constant.Symbol;
import com.crypto.trading.constant.TradingSide;
import com.crypto.trading.dto.TradingPairDto;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class CryptoTradingConfig {
    @Bean
    public CommandLineRunner setupWallet(WalletRepository walletRepository) {
        return args -> {
            Wallet usdtWallet = new Wallet();
            usdtWallet.setUserId(Constant.USER_ID);
            usdtWallet.setBalance(new BigDecimal(50000));
            usdtWallet.setCurrency(Currency.USDT);
            walletRepository.save(usdtWallet);

            Wallet btcWallet = new Wallet();
            btcWallet.setUserId(Constant.USER_ID);
            btcWallet.setBalance(BigDecimal.ZERO);
            btcWallet.setCurrency(Currency.BTC);
            walletRepository.save(btcWallet);

            Wallet ethWallet = new Wallet();
            ethWallet.setUserId(Constant.USER_ID);
            ethWallet.setBalance(BigDecimal.ZERO);
            ethWallet.setCurrency(Currency.ETH);
            walletRepository.save(ethWallet);

        };
    }

    @Bean
    public WebClient webClient() {
        final int size = (int) DataSize.ofMegabytes(16).toBytes();
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public Set<String> supportedSymbols() {
        return Set.of(Symbol.ETHUSDT.name(), Symbol.BTCUSDT.name());
    }

    @Bean
    public Map<TradingSide, Map<Symbol, TradingPairDto>> tradingPairsConfig() {
        Map<TradingSide, Map<Symbol, TradingPairDto>> map = new HashMap<>();
        map.put(TradingSide.SELL, new HashMap<>() {{
            put(Symbol.BTCUSDT, new TradingPairDto(Currency.BTC, Currency.USDT));
            put(Symbol.ETHUSDT, new TradingPairDto(Currency.ETH, Currency.USDT));
        }});
        map.put(TradingSide.BUY, new HashMap<>() {{
            put(Symbol.BTCUSDT, new TradingPairDto(Currency.USDT, Currency.BTC));
            put(Symbol.ETHUSDT, new TradingPairDto(Currency.USDT, Currency.ETH));
        }});
        return map;
    }
}
