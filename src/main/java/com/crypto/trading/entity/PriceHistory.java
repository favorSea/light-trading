package com.crypto.trading.entity;

import com.crypto.trading.entity.id.PriceHistoryId;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_history")
@IdClass(PriceHistoryId.class)
public class PriceHistory {
    @Id
    @Column(nullable = false)
    private String symbol;

    @Id
    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("ask")
    private BigDecimal ask;

    @JsonProperty("ask_src")
    private String askSource;

    @JsonProperty("bid")
    private BigDecimal bid;

    @JsonProperty("bid_src")
    private String bidSource;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public String getAskSource() {
        return askSource;
    }

    public void setAskSource(String askSource) {
        this.askSource = askSource;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public String getBidSource() {
        return bidSource;
    }

    public void setBidSource(String bidSource) {
        this.bidSource = bidSource;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
