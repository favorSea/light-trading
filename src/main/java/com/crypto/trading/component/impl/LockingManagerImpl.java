package com.crypto.trading.component.impl;

import com.crypto.trading.component.LockingManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockingManagerImpl implements LockingManager {
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    @Override
    public void lock(String key) {
        locks.computeIfAbsent(key, id -> new ReentrantLock()).lock();
    }

    @Override
    public void unlock(String key) {
        locks.get(key).unlock();
    }
}
