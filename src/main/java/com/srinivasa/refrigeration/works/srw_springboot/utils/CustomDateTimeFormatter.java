package com.srinivasa.refrigeration.works.srw_springboot.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateTimeFormatter {

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

        int day = dateTime.getDayOfMonth();
        String daySuffix = getDayOfMonthSuffix(day);

        return dateTime.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)) + " " +
                day + daySuffix + ", " +
                dateTime.format(DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH)) +
                " at " +
                dateTime.format(timeFormatter);
    }

    public static String formatWithPeriod(LocalDateTime start, Duration period, ZoneId zone) {
        ZonedDateTime zonedStart = start.atZone(zone);
        ZonedDateTime zonedEnd = zonedStart.plus(period);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("z", Locale.ENGLISH); // <- gives IST, PST, etc.

        int day = zonedStart.getDayOfMonth();
        String daySuffix = getDayOfMonthSuffix(day);

        return zonedStart.format(monthFormatter) + " " +
                day + daySuffix + ", " +
                zonedStart.format(yearFormatter) +
                " from " +
                zonedStart.format(timeFormatter) +
                " to " +
                zonedEnd.format(timeFormatter) +
                " " + zonedStart.format(zoneFormatter);
    }

    public static String formatEndTime(LocalDateTime startTime, Duration period) {
        LocalDateTime end = startTime.plus(period);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        return end.format(timeFormatter);
    }

    public static String formatEndDateTime(LocalDateTime startDateTime, Duration period) {
        LocalDateTime endDateTime = startDateTime.plus(period);
        return formatDateTime(endDateTime);
    }

    private static String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
