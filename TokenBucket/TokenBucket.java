package com.raghvendra;

/**
 * Token-Bucket Algorithm
 */

public class TokenBucket {
    private final long maxBucketSize;
    private final long refillRate;
    private double currentBucketSize;
    private long lastRefillTimestamp;

    public TokenBucket(long maxBucketSize, long refillRate) {
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;

        currentBucketSize = maxBucketSize;
        lastRefillTimestamp = System.nanoTime();
    }

    //synchronized as several threads may be calling this method concurrently
    public synchronized boolean allowRequest(int tokens) {
        //First, refill bucket with token accumulated since the last call
        refill();

        if(currentBucketSize > tokens) {
            currentBucketSize -= tokens;
            return true;
        }

        //request is throttled as there are not enough tokens
        return false;
    }

    private void refill() {
        long now  = System.nanoTime();
        double tokensAdded = (now - lastRefillTimestamp) * refillRate / 1e9;
        currentBucketSize = Math.min(currentBucketSize + tokensAdded, maxBucketSize);
        lastRefillTimestamp = now;
    }
}
