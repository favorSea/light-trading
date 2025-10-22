package com.crypto.trading.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PriceDataDto {
    private Map<String, BigDecimal> asks;
    private Map<String, BigDecimal> bids;
    private final String source;

    public PriceDataDto(Map<String, BigDecimal> asks, Map<String, BigDecimal> bids, String source) {
        this.asks = asks;
        this.bids = bids;
        this.source = source;
    }

    public Map<String, BigDecimal> getAsks() {
        return asks;
    }

    public void setAsks(Map<String, BigDecimal> asks) {
        this.asks = asks;
    }

    public Map<String, BigDecimal> getBids() {
        return bids;
    }

    public void setBids(Map<String, BigDecimal> bids) {
        this.bids = bids;
    }

    public String getSource() {
        return source;
    }
}
