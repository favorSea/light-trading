package com.crypto.trading.controller;

import com.crypto.trading.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @GetMapping("/latest")
    public ResponseEntity<?> latest(@RequestParam String symbol) {
        return priceService.getLatest(symbol.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
