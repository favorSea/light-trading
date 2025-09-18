package com.crypto.trading.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_aggregate")
public class PriceAggregate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @JsonProperty("best_ask")
    private BigDecimal bestAsk;
    @JsonProperty("best_ask_src")
    private String bestAskSource;

    @JsonProperty("best_bid")
    private BigDecimal bestBid;
    @JsonProperty("best_bid_src")
    private String bestBidSource;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(BigDecimal bestAsk) {
        this.bestAsk = bestAsk;
    }

    public String getBestAskSource() {
        return bestAskSource;
    }

    public void setBestAskSource(String bestAskSource) {
        this.bestAskSource = bestAskSource;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }

    public void setBestBid(BigDecimal bestBid) {
        this.bestBid = bestBid;
    }

    public String getBestBidSource() {
        return bestBidSource;
    }

    public void setBestBidSource(String bestBidSource) {
        this.bestBidSource = bestBidSource;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
