package com.crypto.trading.dto;

import java.math.BigDecimal;

public record PriceWitSource(BigDecimal price, String source) {
}
