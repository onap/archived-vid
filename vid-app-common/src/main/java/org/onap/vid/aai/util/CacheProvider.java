package org.onap.vid.aai.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public interface CacheProvider {
    String KEY_DELIMITER = "!@#'";
    /*
    Returns the cache associated with given name; creates one if wasn't any
    */
    <K, V> Cache<K, V> aaiClientCacheFor(String name, Function<K, V> loader);

    /*
    reset cache if exist. Otherwise do nothing
     */
    void resetCache(String name);

    interface Cache<K, V> {
        V get(K key);
    }

    static String compileKey(List<String> args) {
        return compileKey(args.toArray(new String[0]));
    }

    static String compileKey(String... args) {
        return Stream.of(args).map(arg->defaultIfNull(arg, "")).collect( Collectors.joining( KEY_DELIMITER ) );
    }

    static String[] decompileKey(String key) {
        return key.split(KEY_DELIMITER);
    }
}
