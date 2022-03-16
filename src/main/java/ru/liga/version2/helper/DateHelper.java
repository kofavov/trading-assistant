package ru.liga.version2.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateHelper {
    public static boolean checkWeekend(LocalDate date){
        return date.getDayOfWeek().equals(DayOfWeek.MONDAY)
                || date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }


}
