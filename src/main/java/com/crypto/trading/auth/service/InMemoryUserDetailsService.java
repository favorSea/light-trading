package com.crypto.trading.auth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class InMemoryUserDetailsService implements UserDetailsService {

    // simple static users; replace with DB lookup
    private static final Map<String, UserInfo> USERS = Map.of(
            "user", new UserInfo("$2a$10$u1q3rWVY6y2p1lJn7BzqXuNVKfoPtlAAv41C4codIrCGsgONs/hn6", 1L) // password: "password" encoded with BCrypt
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo = USERS.get(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new User(userInfo.id().toString(), userInfo.pwd(), List.of(new OAuth2UserAuthority(Map.of(
                "id", userInfo.pwd()
        ))));
    }

    // TODO move class
    public static record UserInfo(String pwd, Long id) {}
}
