/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.aai.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.properties.Features;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Component
public class CacheProviderWithLoadingCache implements CacheProvider {

    private final ExecutorService cacheReloadPool;
    private final FeatureManager featureManager;
    private final CacheConfigProvider cacheConfigProvider;
    private final ConcurrentHashMap<String, Cache> caches;


    @Autowired
    public CacheProviderWithLoadingCache(FeatureManager featureManager, CacheConfigProvider cacheConfigProvider) {
        this.featureManager = featureManager;
        this.cacheConfigProvider = cacheConfigProvider;
        this.cacheReloadPool = Executors.newFixedThreadPool(3);
        this.caches = new ConcurrentHashMap<>();
    }

    /*
    Returns the cache associated with given name; creates one if wasn't any
     */
    @Override
    public <K, V> Cache<K, V> aaiClientCacheFor(String name, Function<K, V> loader) {
        return (Cache<K, V>) caches.computeIfAbsent(name, s -> buildAaiClientCacheFrom(loader, name));
    }

    @Override
    public void resetCache(String name) {
        caches.remove(name);
    }

    /*
    Creates and returns a Cache that use provided `loader` to fetch values for
    search keys, and stores the result for reuse over a certain time.
    The cache will not use any stored key if FLAG_1810_AAI_LOCAL_CACHE is turned off.
    In that case, `loader` will be invoked for any `get()` request from the cache. The
    cache adheres the flag in real-time; so no restart is required.
     */
    protected <K, V> Cache<K, V> buildAaiClientCacheFrom(Function<K, V> loader, String name) {
        final LoadingCache<K, V> activeCache = buildAaiClientActiveCacheFrom(loader, name);
        return key -> {
            if (featureManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE) &&
                    defaultIfNull(cacheConfigProvider.getCacheConfig(name).isActive(), true)) {
                try {
                    return activeCache.getUnchecked(key);
                }
                catch (UncheckedExecutionException exception) {
                    return ExceptionUtils.rethrow(exception.getCause());
                }
            } else {
                activeCache.invalidateAll();
                activeCache.cleanUp();
                return loader.apply(key);
            }
        };
    }

    private <K, V> LoadingCache<K, V> buildAaiClientActiveCacheFrom(Function<K, V> loader, String name) {
        return createCacheBuilder(name).build(createAsyncReloadingCacheLoaderFrom(loader));

    }

    @NotNull
    protected CacheBuilder<Object, Object> createCacheBuilder(String name) {
        CacheConfig cacheConfig = cacheConfigProvider.getCacheConfig(name);
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(cacheConfig.getExpireAfterWriteHours(), TimeUnit.HOURS)
                .refreshAfterWrite(cacheConfig.getRefreshAfterWriteSeconds(), TimeUnit.SECONDS);
    }

    private <K, V> CacheLoader<K, V> createAsyncReloadingCacheLoaderFrom(Function<K, V> loader) {
        return CacheLoader.asyncReloading(CacheLoader.from(loader::apply), cacheReloadPool);
    }

}
