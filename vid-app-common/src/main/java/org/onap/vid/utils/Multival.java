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

package org.onap.vid.utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@JsonPropertyOrder({"keyType", "valuesType"})
public class Multival<K, V> {
    private final String keyType;
    private final String valuesType;
    private final K key;
    private final Collection<V> values;

    private Multival(String keyType, K key, String valuesType, Collection<V> values) {
        this.keyType = keyType;
        this.key = key;
        this.valuesType = valuesType;
        this.values = values;
    }

    public static <K, V> Multival<K, V> of(String keyType, K key, String valuesType, Collection<V> values) {
        return new Multival<>(keyType, key, valuesType, values);
    }

    public String getKeyType() {
        return keyType;
    }

    public String getValuesType() {
        return valuesType;
    }

    public K getKey() {
        return key;
    }

    public Collection<V> getValues() {
        return values;
    }

    public <W> Multival<K, W> mapEachVal(Function<V, W> mapper) {
        return Multival.of(
                this.getKeyType(),
                this.getKey(),
                this.getValuesType(),
                this.getValues().stream()
                        .map(mapper)
                        .collect(toSet())
        );
    }
}
