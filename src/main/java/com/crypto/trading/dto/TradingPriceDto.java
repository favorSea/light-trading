package com.crypto.trading.dto;

import java.math.BigDecimal;
import java.util.Map;

public record TradingPriceDto(Map<String, BigDecimal> asks, Map<String, BigDecimal> bids) {
}
