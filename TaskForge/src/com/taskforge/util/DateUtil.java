package com.taskforge.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format(LocalDate d) {
        return d == null ? "N/A" : d.format(FMT);
    }
}
