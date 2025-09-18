package com.crypto.trading.service;

import com.crypto.trading.dto.WalletDto;
import com.crypto.trading.entity.Wallet;

import java.util.List;

public interface WalletService {
    List<Wallet> findByUserId(Long userId);

    List<WalletDto> findAllByUserId(Long userId);

    Wallet save(Wallet w);
}
