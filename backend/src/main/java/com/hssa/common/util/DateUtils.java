package com.hssa.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for common date operations.
 */
public final class DateUtils {

    private DateUtils() {}

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
