package com.crypto.trading.dto;

public record TradeResponse(Long id, String symbol, String side, Double price, Double quantity, Double total,
                            String createdAt) {
}
