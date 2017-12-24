package org.openecomp.vid.utils;

import java.util.Arrays;
import java.util.Optional;

import static org.openecomp.vid.utils.Streams.not;

public class Logging {
    public static String getMethodName() {
        return getMethodName(0);
    }

    public static String getMethodCallerName() {
        return getMethodName(1);
    }

    private static String getMethodName(int depth) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String thisClassName = stackTrace[1].getClassName();
        final Optional<String> caller =
                Arrays.stream(stackTrace)
                        .skip(1)
                        .filter(not(frame -> frame.getClassName().equals(thisClassName)))
                        .skip(depth)
                        .map(StackTraceElement::getMethodName)
                        .findFirst();
        return caller.orElse("<unknonwn method name>");
    }


}
