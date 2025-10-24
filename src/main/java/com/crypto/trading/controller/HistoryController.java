package com.crypto.trading.controller;

import com.crypto.trading.constant.Constant;
import com.crypto.trading.dto.TradeDto;
import com.crypto.trading.entity.Trade;
import com.crypto.trading.service.TradeService;
import com.crypto.trading.util.BuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private TradeService tradeService;

    @GetMapping
    public ResponseEntity<List<TradeDto>> history() {
        Long uid = Constant.USER_ID;
        List<TradeDto> trades = tradeService.historyForUser(uid);
        return ResponseEntity.ok(trades);
    }
}
