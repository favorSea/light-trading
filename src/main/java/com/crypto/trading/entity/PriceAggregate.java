package com.crypto.trading.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "price_aggregate")
public class PriceAggregate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    private Double bestAsk;
    private String bestAskSource;

    private Double bestBid;
    private String bestBidSource;

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

    public Double getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(Double bestAsk) {
        this.bestAsk = bestAsk;
    }

    public String getBestAskSource() {
        return bestAskSource;
    }

    public void setBestAskSource(String bestAskSource) {
        this.bestAskSource = bestAskSource;
    }

    public Double getBestBid() {
        return bestBid;
    }

    public void setBestBid(Double bestBid) {
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
