package com.crypto.trading.service;

import com.crypto.trading.repository.TradeRepository;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.entity.PriceAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepo;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PriceService priceService;

    private final Long USER_ID = 1L;

    public Trade executeTrade(String symbol, String side, double quantity) throws Exception {
        Optional<PriceAggregate> agOpt = priceService.getLatest(symbol);
        if (agOpt.isEmpty()) throw new Exception("No price available for " + symbol);
        PriceAggregate ag = agOpt.get();

        double price = 0.0;
        if ("BUY".equalsIgnoreCase(side)) {
            if (ag.getBestAsk() == null) throw new Exception("No ask price available");
            price = ag.getBestAsk();
        } else if ("SELL".equalsIgnoreCase(side)) {
            if (ag.getBestBid() == null) throw new Exception("No bid price available");
            price = ag.getBestBid();
        } else throw new Exception("Invalid side");

        double total = price * quantity;
        Wallet w = walletService.getOrCreate(USER_ID);

        // todo update block by wallet id
        Trade t = new Trade();
        synchronized (w) {
            if ("BUY".equalsIgnoreCase(side)) {
                if (w.getUsdtBalance() < total) throw new Exception("Insufficient USDT");
                w.setUsdtBalance(w.getUsdtBalance() - total);
                if (symbol.equals("ETHUSDT")) {
                    w.setEthBalance(w.getEthBalance() + quantity);
                }
                if (symbol.equals("BTCUSDT")) {
                    w.setBtcBalance(w.getBtcBalance() + quantity);
                }
            } else {
                if (symbol.equals("ETHUSDT")) {
                    if (w.getEthBalance() < quantity) {
                        throw new Exception("Insufficient ETH");
                    }
                    w.setEthBalance(w.getEthBalance() - quantity);
                }
                if (symbol.equals("BTCUSDT")) {
                    if (w.getBtcBalance() < quantity) {
                        throw new Exception("Insufficient BTC");
                    }
                    w.setBtcBalance(w.getBtcBalance() - quantity);
                }
                w.setUsdtBalance(w.getUsdtBalance() + total);
            }

            // TODO move to one transaction
            walletService.save(w);

            t.setUserId(USER_ID);
            t.setSymbol(symbol);
            t.setSide(side.toUpperCase());
            t.setPrice(price);
            t.setQuantity(quantity);
            t.setTotal(total);
            t.setCreatedAt(Instant.now());
            tradeRepo.save(t);
        }

        return t;
    }

    public List<Trade> historyForUser(Long userId) {
        return tradeRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
