package com.crypto.trading.service.impl;

import com.crypto.trading.component.LockingManager;
import com.crypto.trading.constant.Currency;
import com.crypto.trading.constant.Symbol;
import com.crypto.trading.constant.TradingSide;
import com.crypto.trading.dto.TradeDto;
import com.crypto.trading.dto.TradingPairDto;
import com.crypto.trading.entity.PriceAggregate;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.entity.Wallet;
import com.crypto.trading.exception.NotEnoughBalanceException;
import com.crypto.trading.exception.NotFoundPriceException;
import com.crypto.trading.repository.TradeRepository;
import com.crypto.trading.response.TradeResponse;
import com.crypto.trading.service.PriceService;
import com.crypto.trading.service.TradeService;
import com.crypto.trading.service.WalletService;
import com.crypto.trading.util.BuilderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    private TradeRepository tradeRepo;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private LockingManager lockingManager;
    @Autowired
    private Map<TradingSide, Map<Symbol, TradingPairDto>> tradingPairsConfig;

    @Transactional
    @Override
    public TradeResponse executeTrade(Long userId, Symbol symbol, TradingSide side, BigDecimal quantity) throws Exception {

        String lockKey = userId.toString();
        lockingManager.lock(lockKey);
        try {
            BigDecimal price = this.specifyPrice(side, symbol);

            BigDecimal total = price.multiply(quantity);

            TradingPairDto tradingPairDto = tradingPairsConfig.get(side).get(symbol);

            Map<Currency, Wallet> walletMap = this.getWalletsOfUser(userId);
            Wallet fromWallet = walletMap.get(tradingPairDto.from());
            Wallet toWallet = walletMap.get(tradingPairDto.to());
            switch (side) {
                case BUY:
                    if (fromWallet.getBalance().compareTo(total) < 0) {
                        throw new NotEnoughBalanceException("Insufficient " + fromWallet.getCurrency());
                    }
                    fromWallet.setBalance(fromWallet.getBalance().subtract(total));
                    toWallet.setBalance(toWallet.getBalance().add(quantity));
                    break;
                case SELL:
                    if (fromWallet.getBalance().compareTo(quantity) < 0) {
                        throw new NotEnoughBalanceException("Insufficient " + fromWallet.getCurrency());
                    }
                    fromWallet.setBalance(fromWallet.getBalance().subtract(quantity));
                    toWallet.setBalance(toWallet.getBalance().add(total));
                    break;
            }

            walletService.save(fromWallet);
            walletService.save(toWallet);

            Trade trade = new Trade();
            trade.setUserId(userId);
            trade.setSymbol(symbol.name());
            trade.setSide(side.name());
            trade.setPrice(price);
            trade.setQuantity(quantity);
            trade.setTotal(total);
            trade.setCreatedAt(Instant.now());
            tradeRepo.save(trade);

            return new TradeResponse(trade.getId(), trade.getSymbol(), trade.getSide(), trade.getPrice(),
                    trade.getQuantity(), trade.getTotal(), trade.getCreatedAt().toString(), userId);
        } catch (Exception e) {
            LOGGER.error("executeTrade got error: ", e);
            throw new Exception(e);
        } finally {
            lockingManager.unlock(lockKey);
        }
    }

    @Override
    public List<TradeDto> historyForUser(Long userId) {
        List<Trade> trades = tradeRepo.findByUserIdOrderByCreatedAtDesc(userId);
        return BuilderUtils.toTradeDtoList(trades);
    }

    private Map<Currency, Wallet> getWalletsOfUser(Long userId) {
        List<Wallet> wallets = walletService.findByUserId(userId);
        return wallets.stream().collect(Collectors.toMap(Wallet::getCurrency, w -> w));
    }

    private BigDecimal specifyPrice(TradingSide side, Symbol symbol) throws NotFoundPriceException {
        Optional<PriceAggregate> agOpt = priceService.getLatest(symbol.name());
        if (agOpt.isEmpty()) {
            throw new NotFoundPriceException("No price available for " + symbol);
        }
        PriceAggregate ag = agOpt.get();


        return switch (side) {
            case BUY -> {
                if (ag.getBestAsk() == null) {
                    throw new NotFoundPriceException("No ask price available");
                }
                yield ag.getBestAsk();
            }
            case SELL -> {
                if (ag.getBestBid() == null) {
                    throw new NotFoundPriceException("No bid price available");
                }
                yield ag.getBestBid();
            }
        };
    }
}
