package com.crypto.trading.entity.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class PriceHistoryId implements Serializable {

    @Column(nullable = false)
    private String symbol;

    @JsonProperty("created_at")
    private Instant createdAt;

    public PriceHistoryId() {
    }

    public PriceHistoryId(String symbol, Instant createdAt) {
        this.symbol = symbol;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistoryId)) {
            return false;
        }
        PriceHistoryId that = (PriceHistoryId) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, createdAt);
    }
}
