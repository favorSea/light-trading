package com.crypto.trading.util;

public class Utils {
    public static <T> T getOrDefault(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
