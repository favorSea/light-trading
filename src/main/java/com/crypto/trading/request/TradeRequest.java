package com.crypto.trading.request;

import java.math.BigDecimal;

public class TradeRequest {
    private String symbol;
    private String side;
    private BigDecimal quantity;
    private Long userId;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TradeRequest{" +
                "symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", quantity=" + quantity +
                ", userId=" + userId +
                '}';
    }
}
