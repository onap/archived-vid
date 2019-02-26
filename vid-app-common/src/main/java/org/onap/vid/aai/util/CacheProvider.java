/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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
