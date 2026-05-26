package com.example.tracker.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@Slf4j
public class LoggingCache implements Cache {

    private final Cache delegate;

    public LoggingCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override
    public ValueWrapper get(Object key) {

        ValueWrapper value = delegate.get(key);

        if (value != null) {
            log.info("CACHE HIT [{}] key={}", getName(), key);
        } else {
            log.info("CACHE MISS [{}] key={}", getName(), key);
        }

        return value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = delegate.get(key, type);

        if (value != null) {
            log.info("CACHE HIT [{}] key={}", getName(), key);
        } else {
            log.info("CACHE MISS [{}] key={}", getName(), key);
        }

        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        try {
            T value = delegate.get(key, valueLoader);

            if (value != null) {
                log.info("CACHE HIT [{}] key={}", getName(), key);
            } else {
                log.info("CACHE MISS [{}] key={}", getName(), key);
            }

            return value;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        log.info("CACHE PUT [{}] key={}", getName(), key);
        delegate.put(key, value);
    }

    @Override
    public void evict(Object key) {
        log.info("CACHE EVICT [{}] key={}", getName(), key);
        delegate.evict(key);
    }

    @Override
    public void clear() {
        log.info("CACHE CLEAR [{}]", getName());
        delegate.clear();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }
}
