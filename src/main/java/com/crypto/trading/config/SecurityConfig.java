package com.crypto.trading.config;

import com.crypto.trading.auth.filter.JwtAuthFilter;
import com.crypto.trading.constant.UserRole;
import com.crypto.trading.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(JwtUtil jwtUtil,
                                                   HttpSecurity http,
                                                   DaoAuthenticationProvider daoAuthProvider) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        registry ->
                                registry
                                        .requestMatchers("/", "/auth/**", "/api/price/latest").permitAll()
                                        .requestMatchers("/api/trade/**").hasRole(UserRole.USER.name())
                                        .requestMatchers("/api/history/**").hasRole(UserRole.USER.name())
                                        .requestMatchers("/api/wallet/**").hasRole(UserRole.USER.name())
                                        .requestMatchers("/api/price/history").hasRole(UserRole.ADMIN.name())
                                        .anyRequest().authenticated()
                )

                .authenticationProvider(daoAuthProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthProvider) {
        return new ProviderManager(daoAuthProvider);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider(UserDetailsService userDetailsService,
                                                     PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder);
        return p;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
