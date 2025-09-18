package com.crypto.trading.service;

import com.crypto.trading.component.LockingManager;
import com.crypto.trading.dto.TradeDto;
import com.crypto.trading.dto.TradingPairDto;
import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.exception.NotEnoughBalanceException;
import com.crypto.trading.exception.NotFoundPriceException;
import com.crypto.trading.repository.TradeRepository;
import com.crypto.trading.response.TradeResponse;
import com.crypto.trading.util.BuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private TradeRepository tradeRepo;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private LockingManager lockingManager;
    @Autowired
    private Map<String, Map<String, TradingPairDto>> tradingPairsConfig;

    @Transactional
    @Override
    public TradeResponse executeTrade(Long userId, String symbol, String side, BigDecimal quantity) throws Exception {
        Optional<PriceAggregate> agOpt = priceService.getLatest(symbol);
        if (agOpt.isEmpty()) {
            throw new NotFoundPriceException("No price available for " + symbol);
        }
        PriceAggregate ag = agOpt.get();

        BigDecimal price = specifyPrice(side, ag);

        BigDecimal total = price.multiply(quantity);

        TradingPairDto tradingPairDto = tradingPairsConfig.get(side).get(symbol);

        String lockKey = userId.toString();
        lockingManager.lock(lockKey);
        try {
            Map<String, Wallet> walletMap = this.getWalletsOfUser(userId);
            Wallet fromWallet = walletMap.get(tradingPairDto.from());
            Wallet toWallet = walletMap.get(tradingPairDto.to());
            if ("BUY".equals(side)) {
                if (fromWallet.getBalance().compareTo(total) < 0) {
                    throw new NotEnoughBalanceException("Insufficient " + fromWallet.getCurrency());
                }
                fromWallet.setBalance(fromWallet.getBalance().subtract(total));
                toWallet.setBalance(toWallet.getBalance().add(quantity));
            } else {
                if (fromWallet.getBalance().compareTo(quantity) < 0) {
                    throw new NotEnoughBalanceException("Insufficient " + fromWallet.getCurrency());
                }
                fromWallet.setBalance(fromWallet.getBalance().subtract(quantity));
                toWallet.setBalance(toWallet.getBalance().add(total));
            }

            walletService.save(fromWallet);
            walletService.save(toWallet);

            Trade trade = new Trade();
            trade.setUserId(userId);
            trade.setSymbol(symbol);
            trade.setSide(side.toUpperCase());
            trade.setPrice(price);
            trade.setQuantity(quantity);
            trade.setTotal(total);
            trade.setCreatedAt(Instant.now());
            tradeRepo.save(trade);

            return new TradeResponse(trade.getId(), trade.getSymbol(), trade.getSide(), trade.getPrice(),
                    trade.getQuantity(), trade.getTotal(), trade.getCreatedAt().toString());
        } finally {
            lockingManager.unlock(lockKey);
        }
    }

    @Override
    public List<TradeDto> historyForUser(Long userId) {
        List<Trade> trades = tradeRepo.findByUserIdOrderByCreatedAtDesc(userId);
        return BuilderUtils.toTradeDtoList(trades);
    }

    private Map<String, Wallet> getWalletsOfUser(Long userId) {
        List<Wallet> wallets = walletService.findByUserId(userId);

        return wallets.stream().collect(Collectors.toMap(Wallet::getCurrency, w -> w));
    }

    private static BigDecimal specifyPrice(String side, PriceAggregate ag) throws NotFoundPriceException {
        BigDecimal price;
        if ("BUY".equals(side)) {
            if (ag.getBestAsk() == null) {
                throw new NotFoundPriceException("No ask price available");
            }
            price = ag.getBestAsk();
        } else  {
            if (ag.getBestBid() == null) {
                throw new NotFoundPriceException("No bid price available");
            }
            price = ag.getBestBid();
        }
        return price;
    }
}
