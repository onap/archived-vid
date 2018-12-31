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
