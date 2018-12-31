package org.onap.vid.aai.util;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class CacheConfigTest {

    @Test
    public void whenDeserializeJson_ValuesReadAsExpected() {
        CacheConfigProvider cacheConfigProvider = new CacheConfigProviderImpl();
        CacheConfig cacheConfigA = cacheConfigProvider.getCacheConfig("a");
        assertEquals(true, cacheConfigA.isActive());
        assertEquals(6L, cacheConfigA.getExpireAfterWriteHours());
        assertEquals(9L, cacheConfigA.getRefreshAfterWriteSeconds());

        //entry exist in configuration, but with no values
        CacheConfig cacheConfigB = cacheConfigProvider.getCacheConfig("b");
        assertEquals(cacheConfigB.isActive(), CacheConfig.Companion.getDefaultCacheConfig().isActive());
        assertEquals(cacheConfigB.getExpireAfterWriteHours(), CacheConfig.Companion.getDefaultCacheConfig().getExpireAfterWriteHours());
        assertEquals(cacheConfigB.getRefreshAfterWriteSeconds(), CacheConfig.Companion.getDefaultCacheConfig().getRefreshAfterWriteSeconds());


        //entry doesn't exist in configuration
        CacheConfig cacheConfigC = cacheConfigProvider.getCacheConfig("c");
        assertEquals(CacheConfig.Companion.getDefaultCacheConfig(), cacheConfigC);

        CacheConfig cacheConfigD = cacheConfigProvider.getCacheConfig("d");
        assertEquals(false, cacheConfigD.isActive());

    }
}
