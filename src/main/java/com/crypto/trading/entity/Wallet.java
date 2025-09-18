package com.crypto.trading.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private Double usdtBalance;
    private Double btcBalance;
    private Double ethBalance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getUsdtBalance() {
        return usdtBalance;
    }

    public void setUsdtBalance(Double usdtBalance) {
        this.usdtBalance = usdtBalance;
    }

    public Double getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(Double btcBalance) {
        this.btcBalance = btcBalance;
    }

    public Double getEthBalance() {
        return ethBalance;
    }

    public void setEthBalance(Double ethBalance) {
        this.ethBalance = ethBalance;
    }
}
