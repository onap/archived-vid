package org.onap.simulator.util;

public class JsonUtils {

    /**
     * Transforms single-quoted JSON to double-quoted JSON
     **/
    public static String normalizeJson(String input) {
        return input.replaceAll("'", "\"");
    }
}
