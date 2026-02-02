package com.realestate.unit.service;

import com.realestate.service.RateLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitServiceTest {

    private RateLimitService rateLimitService;

    @BeforeEach
    void setUp() {
        rateLimitService = new RateLimitService();
    }

    @Test
    @DisplayName("Should allow first attempt")
    void tryAcquire_FirstAttempt_Success() {
        boolean result = rateLimitService.tryAcquire("test-key");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should allow multiple attempts within limit")
    void tryAcquire_MultipleAttempts_Success() {
        String key = "test-key-multiple";

        for (int i = 0; i < 10; i++) {
            boolean result = rateLimitService.tryAcquire(key);
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should block after exceeding limit")
    void tryAcquire_ExceedLimit_Blocked() {
        String key = "test-key-exceed";

        for (int i = 0; i < 11; i++) {
            rateLimitService.tryAcquire(key);
        }

        boolean result = rateLimitService.tryAcquire(key);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return correct remaining attempts")
    void getRemainingAttempts_Success() {
        String key = "test-key-remaining";

        assertThat(rateLimitService.getRemainingAttempts(key)).isEqualTo(10);

        rateLimitService.tryAcquire(key);
        assertThat(rateLimitService.getRemainingAttempts(key)).isEqualTo(9);

        rateLimitService.tryAcquire(key);
        assertThat(rateLimitService.getRemainingAttempts(key)).isEqualTo(8);
    }

    @Test
    @DisplayName("Should reset rate limit")
    void reset_Success() {
        String key = "test-key-reset";

        for (int i = 0; i < 5; i++) {
            rateLimitService.tryAcquire(key);
        }

        rateLimitService.reset(key);

        assertThat(rateLimitService.getRemainingAttempts(key)).isEqualTo(10);
    }

    @Test
    @DisplayName("Should not be blocked initially")
    void isBlocked_NotBlocked() {
        boolean result = rateLimitService.isBlocked("new-key");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should be blocked after exceeding limit")
    void isBlocked_AfterExceeding() {
        String key = "test-key-blocked";

        for (int i = 0; i < 15; i++) {
            rateLimitService.tryAcquire(key);
        }

        assertThat(rateLimitService.isBlocked(key)).isTrue();
    }

    @Test
    @DisplayName("Should return zero block time when not blocked")
    void getBlockTimeRemaining_NotBlocked() {
        long result = rateLimitService.getBlockTimeRemaining("unblocked-key");

        assertThat(result).isZero();
    }
}
