package com.crypto.trading.request;

import com.crypto.trading.constant.Symbol;
import com.crypto.trading.constant.TradingSide;

import java.math.BigDecimal;

public class TradeRequest {
    private Symbol symbol;
    private TradingSide side;
    private BigDecimal quantity;
    private Long userId;

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public TradingSide getSide() {
        return side;
    }

    public void setSide(TradingSide side) {
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
                '}';
    }
}
