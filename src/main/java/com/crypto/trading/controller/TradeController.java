package com.crypto.trading.controller;

import com.crypto.trading.dto.TradeRequest;
import com.crypto.trading.dto.TradeResponse;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @PostMapping
    public ResponseEntity<?> trade(@RequestBody TradeRequest req) {
        try {
            if (!req.getSymbol().equalsIgnoreCase("ETHUSDT") && !req.getSymbol().equalsIgnoreCase("BTCUSDT"))
                return ResponseEntity.badRequest().body("Unsupported symbol");

            Trade t = tradeService.executeTrade(req.getSymbol().toUpperCase(), req.getSide(), req.getQuantity());
            TradeResponse resp = new TradeResponse(t.getId(), t.getSymbol(), t.getSide(), t.getPrice(), t.getQuantity(),
                    t.getTotal(), t.getCreatedAt().toString());
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
