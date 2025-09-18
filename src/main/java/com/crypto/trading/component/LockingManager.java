package com.crypto.trading.component;

public interface LockingManager {
    void lock(String key);

    void unlock(String key);
}
