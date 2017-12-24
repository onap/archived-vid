package org.openecomp.vid.utils;

import java.util.function.Predicate;

public class Streams {
    public static <R> Predicate<R> not(Predicate<R> predicate) {
        return predicate.negate();
    }
}
