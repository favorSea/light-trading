package com.crypto.trading.provider;

import com.crypto.trading.dto.PriceDataDto;

public interface PriceProvider {
    PriceDataDto getLatestPrice();
}
