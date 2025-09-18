package com.crypto.trading.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record TradeResponse(Long id, String symbol, String side, BigDecimal price, BigDecimal quantity, BigDecimal total,
                            @JsonProperty("created_at") String createdAt) {
}
