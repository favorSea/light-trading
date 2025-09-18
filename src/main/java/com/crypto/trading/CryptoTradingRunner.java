package com.crypto.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingRunner {
    public static void main(String[] args) {
        SpringApplication.run(CryptoTradingRunner.class, args);
    }
}
