package org.tic.guice.cache;

import com.google.common.cache.AbstractCache;

import java.util.HashMap;
import java.util.Map;

public class GuiceDemoCache extends AbstractCache<String, String> {

    private final Map<String, String> keyValues = new HashMap<>();

    @Override
    public String getIfPresent(Object key) {
        return this.keyValues.get(key);
    }

    @Override
    public void put(String key, String value) {
        keyValues.put(key, value);
    }


}
