package com.crypto.trading.repository;

import com.crypto.trading.entity.PriceAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceAggregateRepository extends JpaRepository<PriceAggregate, Long> {
    Optional<PriceAggregate> findBySymbol(String symbol);
}
