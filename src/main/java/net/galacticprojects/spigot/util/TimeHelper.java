package net.galacticprojects.spigot.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.util.HashMap;

import static java.time.temporal.ChronoField.*;

public class TimeHelper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final DateTimeFormatter BAN_TIME_FORMATTER;



    static {
        HashMap<Long, String> dow = new HashMap<>();
        dow.put(1L, "Monday");
        dow.put(2L, "Tuesday");
        dow.put(3L, "Wednesday");
        dow.put(4L, "Thursday");
        dow.put(5L, "Friday");
        dow.put(6L, "Saturday");
        dow.put(7L, "Sunday");
        HashMap<Long, String> moy = new HashMap<>();
        moy.put(1L, "January");
        moy.put(2L, "February");
        moy.put(3L, "March");
        moy.put(4L, "April");
        moy.put(5L, "May");
        moy.put(6L, "June");
        moy.put(7L, "July");
        moy.put(8L, "August");
        moy.put(9L, "September");
        moy.put(10L, "October");
        moy.put(11L, "November");
        moy.put(12L, "December");
        BAN_TIME_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().appendText(DAY_OF_WEEK, dow)
                .appendLiteral(", ").appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(". ")
                .appendText(MONTH_OF_YEAR, moy).appendLiteral(' ').appendValue(YEAR, 4, 16, SignStyle.NOT_NEGATIVE).appendLiteral(" - ").appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2).appendLiteral(" (")
                .appendZoneText(TextStyle.SHORT).appendLiteral(')').toFormatter().withZone(ZoneId.of("UTC"));
    }

    private TimeHelper() {
        throw new UnsupportedOperationException();
    }

    public static OffsetDateTime fromString(final String string) {
        return string == null ? null : OffsetDateTime.parse(string, FORMATTER);
    }

    public static String toString(final OffsetDateTime time) {
        return time == null ? null : FORMATTER.format(time);
    }
}