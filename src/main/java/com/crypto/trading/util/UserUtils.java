package com.crypto.trading.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static Long getUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
