package com.hunghq.librarymanagement.Global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting date and time values in the application.
 * Provides standardized formatting to ensure consistent date representations.
 */
public class Format {

    /**
     * Formats a LocalDateTime object to a string in the pattern "dd-MM-yyyy HH:mm:ss".
     *
     * @param date the LocalDateTime object to format
     * @return a formatted date string in "dd-MM-yyyy HH:mm:ss" format
     */
    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }

}
