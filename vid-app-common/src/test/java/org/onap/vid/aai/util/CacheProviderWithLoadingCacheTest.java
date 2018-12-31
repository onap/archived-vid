package org.onap.vid.aai.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.properties.Features;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

public class CacheProviderWithLoadingCacheTest {
    private final FeatureManager featureManager = mock(FeatureManager.class);
    private final CacheConfigProvider cacheConfigProvider = mock(CacheConfigProvider.class);

    @BeforeMethod
    public void activateCacheFeatureFlag() {
        reset(featureManager);
        when(featureManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)).thenReturn(true);
        when(cacheConfigProvider.getCacheConfig(any())).thenReturn(CacheConfig.Companion.getDefaultCacheConfig());
    }

    private CacheProviderWithLoadingCache createNewCacheProvider() {
        return new CacheProviderWithLoadingCache(featureManager, cacheConfigProvider);
    }

    private String RAND() {
        return randomAlphanumeric(5);
    }

    @Test
    public void cacheProvider_requestingCache_CreatesNewCache() {
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(RAND(), (Integer i) -> i + 4);

        assertThat(integerIntegerCache, notNullValue());
        assertThat(integerIntegerCache.get(5), is(9));
    }

    @Test
    public void cacheProvider_requestingCacheSameNameTwice_ReturnsFirstCacheInstance() {
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final String name = RAND();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(name, (Integer i) -> i + 4);
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache2 = provider.aaiClientCacheFor(name, (Integer i) -> i + 6);

        assertThat(integerIntegerCache2, sameInstance(integerIntegerCache));
        assertThat(integerIntegerCache.get(5), is(9)); // as the first one
    }

    @Test
    public void cacheProvider_requestingCacheSameNameTwiceOutOfSeveral_ReturnsFirstCacheInstance() {
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();

        final String name = RAND();
        provider.aaiClientCacheFor(RAND(), (Integer i) -> i + 2);
        provider.aaiClientCacheFor(RAND(), (Integer i) -> i + 3);
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(name, (Integer i) -> i + 4);
        provider.aaiClientCacheFor(RAND(), (Integer i) -> i + 5);
        provider.aaiClientCacheFor(RAND(), (Integer i) -> i + 6);
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache2 = provider.aaiClientCacheFor(name, (Integer i) -> i + 4);

        assertThat(integerIntegerCache2, sameInstance(integerIntegerCache));
    }

    @Test
    public void cacheProvider_sameProviderSupportDifferentKV() {
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();

        assertThat(provider.aaiClientCacheFor(RAND(),
                (Integer i) -> i + 2).get(0), is(2));
        assertThat(provider.aaiClientCacheFor(RAND(),
                (Integer i) -> i + 3).get(0), is(3));
        assertThat(provider.aaiClientCacheFor(RAND(),
                (String s) -> s + 5).get("0"), is("05"));
        assertThat(provider.aaiClientCacheFor(RAND(),
                (Integer i) -> "0" + i).get(0), is("00"));
        assertThat(provider.aaiClientCacheFor(RAND(),
                (Pair p) -> ImmutableList.of(p.getLeft(), p.getRight())).get(Pair.of(7, "B")), contains(7, "B"));
    }

    @Test
    public void cache_callMultiTimesGetFromCahce_loaderCalledOncePairValue(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(RAND(), (Integer i) -> RandomUtils.nextInt());
        int key = RandomUtils.nextInt();
        Integer result1 = integerIntegerCache.get(key);
        Integer result2 = integerIntegerCache.get(key + 1);
        Integer result3 = integerIntegerCache.get(key);

        Assert.assertNotEquals(result1,result2);
        Assert.assertEquals(result1,result3);
    }

    @Test
    public void cache_callMultiTimesGetFromCahce_loaderCalledOnce(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final MutableInt counter = new MutableInt();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(RAND(), (Integer i) -> {
            counter.increment();
            return i;
        });

        int key = RandomUtils.nextInt();
        Integer result1 = integerIntegerCache.get(key);
        Integer result2 = integerIntegerCache.get(key);
        Integer result3 = integerIntegerCache.get(key);

        Assert.assertEquals(result1.intValue(),key);
        Assert.assertEquals(result2.intValue(),key);
        Assert.assertEquals(result3.intValue(),key);
        Assert.assertEquals(counter.intValue(),1);
    }

    @Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp ="boom" )
    public void cache_inCaseLoaderMethodThrowsException_cacheThrowsSameException(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final CacheProvider.Cache<String, Integer> stringIntegerCache = provider.aaiClientCacheFor(RAND(), (String s) -> { throw new GenericUncheckedException("boom");});

        stringIntegerCache.get("Whatever");
    }

    @Test
    public void cache_inCaseLoaderMethodThrowsException_nextCallUseLoaderMethod(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(RAND(), new Function<Integer, Integer>() {
            private boolean firstTime = true;
            @Override
            public Integer apply(Integer i) {
                if (firstTime) {
                    firstTime = false;
                    throw new GenericUncheckedException("boom");
                }
                else {
                    return i;
                }
            }
        });
      
        try {
            integerIntegerCache.get(1);
        }
        catch (GenericUncheckedException e) {}

        assertEquals(new Integer(1), integerIntegerCache.get(1));
    }

    @Test
    public void cache_getIsCalledMoreThanOnce_loaderNotCalledAgainForSameInputValue(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final MutableInt counter = new MutableInt();
        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(RAND(), (Integer i) -> { counter.increment(); return i; });

        int key1 = RandomUtils.nextInt();
        int key2 = RandomUtils.nextInt();
        integerIntegerCache.get(key1);
        Assert.assertEquals(counter.intValue(),1);
        integerIntegerCache.get(key2);
        Assert.assertEquals(counter.intValue(),2);
        integerIntegerCache.get(key1);
        Assert.assertEquals(counter.intValue(),2);
    }

    @Test
    public void cache_getIsCalledMoreThanOnce_loaderIsCalledAgainAfterReset(){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final MutableInt counter = new MutableInt();
        String cacheName = RAND();

        int key1 = RandomUtils.nextInt();
        provider.aaiClientCacheFor(cacheName, (Integer i) -> { counter.increment(); return i; }).get(key1);
        Assert.assertEquals(counter.intValue(), 1);
        provider.aaiClientCacheFor(cacheName, (Integer i) -> { counter.increment(); return i; }).get(key1);
        Assert.assertEquals(counter.intValue(), 1);

        provider.resetCache(cacheName);
        provider.aaiClientCacheFor(cacheName, (Integer i) -> { counter.increment(); return i; }).get(key1);
        Assert.assertEquals(counter.intValue(), 2);

    }


    public static class TestData {
        public FeatureManager featureManager;
        public String cacheName;
        public CacheConfigProvider cacheConfigProvider;

        public TestData(FeatureManager featureManager, String cacheName, CacheConfigProvider cacheConfigProvider) {
            this.featureManager = featureManager;
            this.cacheName = cacheName;
            this.cacheConfigProvider = cacheConfigProvider;
        }
    }


    @DataProvider
    public static Object[][] mockForCacheIsNotActive() {

        Consumer<TestData> mockFeatureOff = (testData)->{
            when(testData.featureManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)).thenReturn(false);
            when(testData.cacheConfigProvider.getCacheConfig(testData.cacheName)).thenReturn(new CacheConfig(true, 10L, 10L));
        };
        Consumer<TestData> mockFeatureOnCacheOff = (testData)->{
            when(testData.featureManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)).thenReturn(true);
            when(testData.cacheConfigProvider.getCacheConfig(testData.cacheName)).thenReturn(new CacheConfig(false, 10L, 10L));
        };


        return new Object[][]{
                {mockFeatureOff},
                {mockFeatureOnCacheOff}
        };
    }


    @Test(dataProvider = "mockForCacheIsNotActive")
    public void cache_featureFlagToggleIsOff_loaderIsCalledForEachGet(Consumer<TestData> mocker){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final MutableInt counter = new MutableInt();
        String cacheName = RAND();

        final CacheProvider.Cache<Integer, Integer> integerIntegerCache = provider.aaiClientCacheFor(cacheName, (Integer i) -> {
            counter.increment();
            return i;
        });

        mocker.accept(new TestData(featureManager, cacheName, cacheConfigProvider));
        counter.setValue(0);
        int key = RandomUtils.nextInt();
        integerIntegerCache.get(key);
        integerIntegerCache.get(key);

        Assert.assertEquals(counter.intValue(),2);
    }

    @Test
    public void cache_loaderReturnsRandomValue_sameValueIsReturnedForSameKey() {
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final String name = RAND();
        final CacheProvider.Cache<Integer, String> integerRandomStringCache = provider.aaiClientCacheFor(name, (Integer i) -> RAND());

        String firstGet = integerRandomStringCache.get(1);
        String secondGet = integerRandomStringCache.get(1);

        Assert.assertEquals(firstGet,secondGet);
    }

    @Test(dataProvider = "mockForCacheIsNotActive")
    public void cache_toggleFlagOff_ResetCache(Consumer<TestData> mocker){
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        final String cacheName = RAND();
        final CacheProvider.Cache<Integer, String> integerRandomStringCache = provider.aaiClientCacheFor(cacheName, (Integer i) -> RAND());
        int key = RandomUtils.nextInt();
        String result1 = integerRandomStringCache.get(key);
        String result2 = integerRandomStringCache.get(key);
        Assert.assertEquals(result1,result2);

        mocker.accept(new TestData(featureManager, cacheName, cacheConfigProvider));
        String result3 = integerRandomStringCache.get(key);
        Assert.assertNotEquals(result1, result3);
    }

    @DataProvider
    public static Object[][] mockForCacheBuilderConfig() {
        return new Object[][]{
                {2L, 2L, 3L, 3L},
                {null, 10L, null, 24L} //null meaning use the default, which is 10L,24L
        };
    }

    @Test(dataProvider = "mockForCacheBuilderConfig")
    public void cacheBuilderConfiguredWithValues_andWithDefaults(
            Long refreshAfterWriteSeconds,
            long expectedRefreshAfterWriteSeconds,
            Long expireAfterWriteHours,
            long expectedExpireAfterWriteHours
            ) throws IllegalAccessException {
        final String cacheName = RAND();
        when(cacheConfigProvider.getCacheConfig(cacheName)).thenReturn(new CacheConfig(true, expireAfterWriteHours, refreshAfterWriteSeconds));
        final CacheProviderWithLoadingCache provider = createNewCacheProvider();
        CacheBuilder<Object, Object> cacheBuilder = provider.createCacheBuilder(cacheName);


        //Unfortunately CacheBuilder doesn't expose public getters
        //Since it's only unit test I let myself do some fouls and use reflection
        Long actualRefreshNanos = (Long)FieldUtils.readDeclaredField(cacheBuilder, "refreshNanos", true);
        assertThat(actualRefreshNanos, equalTo(TimeUnit.NANOSECONDS.convert(expectedRefreshAfterWriteSeconds, TimeUnit.SECONDS)));

        Long actualExpireAfterWriteNanos = (Long)FieldUtils.readDeclaredField(cacheBuilder, "expireAfterWriteNanos", true);
        assertThat(actualExpireAfterWriteNanos, equalTo(TimeUnit.NANOSECONDS.convert(expectedExpireAfterWriteHours, TimeUnit.HOURS)));

    }
}

