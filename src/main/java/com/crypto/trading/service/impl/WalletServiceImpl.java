package com.crypto.trading.service.impl;

import com.crypto.trading.dto.WalletDto;
import com.crypto.trading.repository.WalletRepository;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.service.WalletService;
import com.crypto.trading.util.BuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository repo;

    @Override
    public List<Wallet> findByUserId(Long userId) {
        return repo.findAllByUserId(userId);
    }

    @Override
    public List<WalletDto> findAllByUserId(Long userId) {
        var wallets = this.findByUserId(userId);
        return BuilderUtils.toWalletDtoList(wallets);
    }

    @Override
    public Wallet save(Wallet w) {
        return repo.save(w);
    }
}
