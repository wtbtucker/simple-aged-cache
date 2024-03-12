package io.collective;

import java.time.Clock;
import java.util.HashMap;
public class SimpleAgedCache {
    private Clock clock;
    private HashMap<Object, ExpirableEntry> cache;

    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
        this.cache = new HashMap<>();
    }

    public SimpleAgedCache() {
        this(Clock.systemDefaultZone());
    }

    public void put(Object key, Object value, int retentionInMillis) {
        ExpirableEntry entry = new ExpirableEntry(value, clock.millis() + retentionInMillis);
        cache.put(key, entry);
    }

    public boolean isEmpty() {
        cleanCache();
        return cache.isEmpty();
    }

    public int size() {
        cleanCache();
        return cache.size();
    }

    public Object get(Object key) {
        cleanCache();
        ExpirableEntry entry = cache.get(key);
        return (entry != null) ? entry.value : null;
    }

    private void cleanCache() {
        long currentTime = clock.millis();
        for (var entry : cache.entrySet()) {
            if (entry.getValue().isExpired(currentTime)) {
                cache.remove(entry.getKey());
            }
        }
    }

    private static class ExpirableEntry {
        Object value;
        long expirationTime;

        ExpirableEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        boolean isExpired(long currentTime) {
            return currentTime >= expirationTime;
        }
    }
}