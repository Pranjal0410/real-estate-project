package com.realestate.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class RateLimitService {

    private static final int MAX_ATTEMPTS = 10;
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(15);
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(30);

    private final Cache<String, AtomicInteger> attemptCache;
    private final Cache<String, Long> blockCache;

    public RateLimitService() {
        this.attemptCache = Caffeine.newBuilder()
                .expireAfterWrite(WINDOW_DURATION)
                .maximumSize(10000)
                .build();

        this.blockCache = Caffeine.newBuilder()
                .expireAfterWrite(BLOCK_DURATION)
                .maximumSize(10000)
                .build();
    }

    public boolean isBlocked(String key) {
        Long blockedUntil = blockCache.getIfPresent(key);
        if (blockedUntil != null && System.currentTimeMillis() < blockedUntil) {
            return true;
        }
        return false;
    }

    public boolean tryAcquire(String key) {
        if (isBlocked(key)) {
            log.warn("Rate limit: {} is blocked", key);
            return false;
        }

        AtomicInteger attempts = attemptCache.get(key, k -> new AtomicInteger(0));
        int currentAttempts = attempts.incrementAndGet();

        if (currentAttempts > MAX_ATTEMPTS) {
            blockCache.put(key, System.currentTimeMillis() + BLOCK_DURATION.toMillis());
            log.warn("Rate limit exceeded for {}, blocking for {} minutes", key, BLOCK_DURATION.toMinutes());
            return false;
        }

        return true;
    }

    public void reset(String key) {
        attemptCache.invalidate(key);
        blockCache.invalidate(key);
    }

    public int getRemainingAttempts(String key) {
        AtomicInteger attempts = attemptCache.getIfPresent(key);
        if (attempts == null) {
            return MAX_ATTEMPTS;
        }
        return Math.max(0, MAX_ATTEMPTS - attempts.get());
    }

    public long getBlockTimeRemaining(String key) {
        Long blockedUntil = blockCache.getIfPresent(key);
        if (blockedUntil == null) {
            return 0;
        }
        return Math.max(0, blockedUntil - System.currentTimeMillis());
    }
}
