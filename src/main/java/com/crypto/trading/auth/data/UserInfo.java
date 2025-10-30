package com.crypto.trading.auth.data;

import com.crypto.trading.constant.UserRole;

public record UserInfo(String pwd, Long id, UserRole userRole) {}
