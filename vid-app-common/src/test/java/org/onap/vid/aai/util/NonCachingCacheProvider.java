package org.onap.vid.aai.util;

import java.util.function.Function;

public class NonCachingCacheProvider implements CacheProvider {

    @Override
    public <K, V> Cache<K, V> aaiClientCacheFor(String name, Function<K, V> loader) {
        return loader::apply;
    }

    @Override
    public void resetCache(String name) {}
}
