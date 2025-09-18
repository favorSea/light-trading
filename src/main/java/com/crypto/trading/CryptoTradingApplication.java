package com.crypto.trading;

import com.crypto.trading.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.crypto.trading.entity.Wallet;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptoTradingApplication.class, args);
    }

    @Bean
    CommandLineRunner init(WalletRepository walletRepository) {
        return args -> {
            if (walletRepository.findByUserId(1L).isEmpty()) {
                Wallet w = new Wallet();
                w.setUserId(1L);
                w.setUsdtBalance(50000.0);
                w.setBtcBalance(0.0);
                w.setEthBalance(0.0);
                walletRepository.save(w);
            }
        };
    }

    @Bean
    WebClient webClient() {
        final int size = (int) DataSize.ofMegabytes(16).toBytes();
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
