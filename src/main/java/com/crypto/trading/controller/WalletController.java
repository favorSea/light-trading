package com.crypto.trading.controller;

import com.crypto.trading.entity.Wallet;
import com.crypto.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<?> get() {
        Wallet w = walletService.getOrCreate(1L);
        return ResponseEntity.ok(w);
    }
}
