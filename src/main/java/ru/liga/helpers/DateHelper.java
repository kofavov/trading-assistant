package ru.liga.helpers;

import org.apache.commons.math3.util.Precision;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

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
//    сколько дней добавить
    public static int getCountDays(Request request) {
        if (request.getTimeFrame().equals("tomorrow")) {
            return 1;
        } else if (request.getTimeFrame().equals("week")) {
            return 7;
        }
//        else if (request.getTimeFrame().equals("month")){
//            return 30;
//        }
        return 0;
    }
    public static boolean newCheckDayOfWeek(LocalDate localDate){
        return localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)
                ||localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
}
