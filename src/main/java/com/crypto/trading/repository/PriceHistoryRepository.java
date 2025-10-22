package com.crypto.trading.repository;

import com.crypto.trading.entity.PriceHistory;
import com.crypto.trading.entity.id.PriceHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, PriceHistoryId> {
    List<PriceHistory> findAllBySymbolOrderByCreatedAtDesc(String symbol);
}
