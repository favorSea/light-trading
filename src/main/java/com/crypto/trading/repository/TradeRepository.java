package com.crypto.trading.repository;

import com.crypto.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserIdOrderByCreatedAtDesc(Long userId);
}
