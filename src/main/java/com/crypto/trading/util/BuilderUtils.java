package com.crypto.trading.util;

import com.crypto.trading.dto.TradeDto;
import com.crypto.trading.dto.WalletDto;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.entity.Wallet;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class BuilderUtils {
    public static TradeDto toTradeDto(Trade trade) {
        TradeDto dto = new TradeDto();
        dto.setId(trade.getId());
        dto.setUserId(trade.getUserId());
        dto.setSymbol(trade.getSymbol());
        dto.setSide(trade.getSide());
        dto.setPrice(trade.getPrice());
        dto.setQuantity(trade.getQuantity());
        dto.setTotal(trade.getTotal());
        dto.setCreatedAt(trade.getCreatedAt());
        return dto;
    }

    public static List<TradeDto> toTradeDtoList(List<Trade> trades) {
        List<TradeDto> dtoList = new LinkedList<>();
        if (CollectionUtils.isEmpty(trades)) {
            return dtoList;
        }
        for (var trade : trades) {
            TradeDto dto = BuilderUtils.toTradeDto(trade);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static WalletDto toWalletDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setUserId(wallet.getUserId());
        dto.setCurrency(wallet.getCurrency().name());
        dto.setBalance(wallet.getBalance());
        return dto;
    }

    public static List<WalletDto> toWalletDtoList(List<Wallet> wallets) {
        List<WalletDto> dtoList = new LinkedList<>();
        if (CollectionUtils.isEmpty(wallets)) {
            return dtoList;
        }
        for (var wallet : wallets) {
            WalletDto dto = BuilderUtils.toWalletDto(wallet);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
