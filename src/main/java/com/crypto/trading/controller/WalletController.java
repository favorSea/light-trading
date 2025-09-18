package com.crypto.trading.controller;

import com.crypto.trading.constant.Constant;
import com.crypto.trading.dto.WalletDto;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<List<WalletDto>> get(@RequestParam(required = false) Long userId) {
        Long uid = (userId == null) ? Constant.USER_ID : userId;
        List<WalletDto> walletList = walletService.findAllByUserId(uid);
        return ResponseEntity.ok(walletList);
    }
}
