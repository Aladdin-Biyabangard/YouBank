package com.aladdin.youbank001.utils;

import java.time.LocalDate;

public class DateTimeUtil {

    public static LocalDate startOfDay(Integer year, Integer month, Integer day) {
        return LocalDate.of(
                year,
                month,
                day);
    }

    public static LocalDate endOfDay(Integer year, Integer month, Integer day) {
        return LocalDate.of(year, month, day);
    }
}
