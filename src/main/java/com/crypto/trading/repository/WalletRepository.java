package com.crypto.trading.repository;

import com.crypto.trading.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllByUserId(Long userId);
}
