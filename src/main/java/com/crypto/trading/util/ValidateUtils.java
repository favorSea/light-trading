package com.crypto.trading.util;

import com.crypto.trading.exception.InvalidRequestException;
import com.crypto.trading.request.TradeRequest;

import java.math.BigDecimal;
import java.util.Set;

public class ValidateUtils {
    public static void verifyTradeRequest(TradeRequest req, Set<String> supportedSymbols) throws InvalidRequestException {
        if (req.getSymbol() == null || !supportedSymbols.contains(req.getSymbol().name())) {
            throw new InvalidRequestException("Invalid symbol " + req.getSymbol());
        }
        if (req.getSide() == null) {
            throw new InvalidRequestException("Invalid side ");
        }
        if (req.getQuantity() == null || req.getQuantity().compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidRequestException("Invalid quantity " + req.getQuantity());
        }
    }
}
