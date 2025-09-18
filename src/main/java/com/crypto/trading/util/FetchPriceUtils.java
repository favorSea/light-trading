package com.crypto.trading.util;

import com.crypto.trading.dto.TradingPriceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FetchPriceUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T getOrDefault(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static TradingPriceDto parsePricesResponse(String rawResponse, Set<String> supportedSymbols,
                                                      String askPriceField,
                                                      String bidPriceField)
            throws JsonProcessingException {
        Map<String, BigDecimal> asks = new HashMap<>();
        Map<String, BigDecimal> bids = new HashMap<>();

        JsonNode jsonNodeRoot = MAPPER.readTree(rawResponse);
        if (jsonNodeRoot.isArray()) {
            for (JsonNode jsonNode : jsonNodeRoot) {
                String symbol = jsonNode.path("symbol").asText();
                if (supportedSymbols.contains(symbol)) {
                    BigDecimal ask = new BigDecimal(jsonNode.path(askPriceField).asText());
                    BigDecimal bid = new BigDecimal(jsonNode.path(bidPriceField).asText());
                    asks.put(symbol, ask);
                    bids.put(symbol, bid);
                }
            }
        }

        return new TradingPriceDto(asks, bids);
    }
}
