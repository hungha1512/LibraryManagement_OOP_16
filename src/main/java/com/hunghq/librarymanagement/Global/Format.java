package com.hunghq.librarymanagement.Global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String formatInput(String input) {
        List<String> genres = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.toList());
        return genres.toString();
    }
}
