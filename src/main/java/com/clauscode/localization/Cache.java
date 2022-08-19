package com.clauscode.localization;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public Cache(int capacity) {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > capacity;
    }
}
