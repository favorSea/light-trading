package com.crypto.trading.service;

import com.crypto.trading.dto.TradeDto;
import com.crypto.trading.response.TradeResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TradeService {
    TradeResponse executeTrade(Long userId, String symbol, String side, BigDecimal quantity) throws Exception;

    List<TradeDto> historyForUser(Long userId);
}
