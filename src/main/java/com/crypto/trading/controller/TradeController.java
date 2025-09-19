package com.crypto.trading.controller;

import com.crypto.trading.constant.Constant;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.request.TradeRequest;
import com.crypto.trading.response.TradeResponse;
import com.crypto.trading.service.TradeService;
import com.crypto.trading.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;
    @Autowired
    private Set<String> supportedSymbols;

    @PostMapping
    public ResponseEntity<?> trade(@RequestBody TradeRequest req) {
        try {
            LOGGER.info("Received trade request: {}", req);
            ValidateUtils.verifyTradeRequest(req, supportedSymbols);
            Long uid = (req.getUserId() == null) ? Constant.USER_ID : req.getUserId();
            TradeResponse resp = tradeService.executeTrade(uid, req.getSymbol(), req.getSide(),
                    req.getQuantity());
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
