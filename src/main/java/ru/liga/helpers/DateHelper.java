package ru.liga.helpers;

import ru.liga.model.Case;
import ru.liga.model.Request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    /**
     * Так как по выходным биржа не работает вс и сб пропускаются(в данном случае вс и пн)
     */
    public static int checkDayOfWeek(Case c) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE");
        String curDate = c.getDate().format(dateTimeFormatter);
        if (curDate.contains("сб")) return 3;
        else if (curDate.contains("вс")) return 2;
        return 1;
    }
    public static boolean checkWeekend(LocalDate date){
        return date.getDayOfWeek().equals(DayOfWeek.MONDAY)
                || date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
//    сколько дней добавить
    public static int getCountDays(Request request) {
        if (request.getPeriod().equals("tomorrow")) {
            return 1;
        } else if (request.getPeriod().equals("week")) {
            return 7;
        }
        else if (request.getPeriod().equals("month")){
            return 30;
        }
        else if (!request.getDate().equals(LocalDate.now()))
        {
            return request.getDate().compareTo(LocalDate.now());
        }
        return 0;
    }
    public static LocalDate checkDayOfWeek(LocalDate tempDay) {
        if (tempDay.getDayOfWeek().equals(DayOfWeek.MONDAY))
            tempDay = tempDay.minusDays(2);
        else if (tempDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            tempDay = tempDay.minusDays(1);
        }
        return tempDay;
    }

    public static boolean newCheckDayOfWeek(LocalDate localDate){
        return localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)
                ||localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
}
