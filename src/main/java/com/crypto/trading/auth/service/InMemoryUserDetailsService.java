package com.crypto.trading.auth.service;

import com.crypto.trading.auth.data.UserInfo;
import com.crypto.trading.constant.UserRole;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
public class InMemoryUserDetailsService implements UserDetailsService {

    // simple static users; replace with DB lookup
    private static final Map<String, UserInfo> USERS = Map.of(
            // password: "password" encoded with BCrypt
            "user", new UserInfo("$2a$10$u1q3rWVY6y2p1lJn7BzqXuNVKfoPtlAAv41C4codIrCGsgONs/hn6", 1L, UserRole.USER),
            "admin", new UserInfo("$2a$10$u1q3rWVY6y2p1lJn7BzqXuNVKfoPtlAAv41C4codIrCGsgONs/hn6", 10L, UserRole.ADMIN),
            "user2", new UserInfo("$2a$10$u1q3rWVY6y2p1lJn7BzqXuNVKfoPtlAAv41C4codIrCGsgONs/hn6", 2L, UserRole.USER),
            "guest", new UserInfo("$2a$10$u1q3rWVY6y2p1lJn7BzqXuNVKfoPtlAAv41C4codIrCGsgONs/hn6", 1L, UserRole.GUEST)

    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo = USERS.get(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.builder()
                .username(userInfo.id().toString())
                .password(userInfo.pwd())
                .roles(userInfo.userRole().name())
                .build();
    }
}
