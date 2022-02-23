package ru.liga.helpers;

import org.apache.commons.math3.util.Precision;
import ru.liga.model.Case;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class DateHelper {
    /**
     * так как по выходным биржа не работает надо пропустить вс и сб(в данном случае вс и пн)
     *     в идеале и праздники
     */
    public static int checkDayOfWeek(Case c) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE");
        String curDate = c.getDate().format(dateTimeFormatter);
        if (curDate.contains("сб")) return 3;
        else if (curDate.contains("вс")) return 2;
        return 1;
    }
//    сколько дней добавить
    public static int getCountDays(String[] request) {
        if (request[2].equals("tomorrow")) {
            return 1;
        } else if (request[2].equals("week")) {
            return 7;
        }
        return 0;
    }
}
