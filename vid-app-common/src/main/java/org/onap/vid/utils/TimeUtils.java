package org.onap.vid.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

    private TimeUtils() {
        // explicit private constructor, to hide the implicit public constructor
    }

    public static ZonedDateTime parseZonedDateTime(String time) {

        return ZonedDateTime.from(formatter.parse(time));
    }

    public static String zonedDateTimeToString(ZonedDateTime time) {
        return formatter.format(time);
    }
}
