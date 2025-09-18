package com.crypto.trading.service;

import com.crypto.trading.repository.WalletRepository;
import com.crypto.trading.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    private WalletRepository repo;

    public Wallet getOrCreate(Long userId) {
        return repo.findByUserId(userId).orElseGet(() -> {
            Wallet w = new Wallet();
            w.setUserId(userId);
            w.setUsdtBalance(50000.0);
            w.setBtcBalance(0.0);
            w.setEthBalance(0.0);
            return repo.save(w);
        });
    }

    public Wallet save(Wallet w) { return repo.save(w); }
}
