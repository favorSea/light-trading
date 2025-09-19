package com.crypto.trading.dto;

import com.crypto.trading.constant.Currency;

public record TradingPairDto(Currency from, Currency to) {
}
