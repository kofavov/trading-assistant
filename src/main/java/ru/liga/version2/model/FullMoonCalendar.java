package ru.liga.version2.model;


import ru.liga.version2.parser.CSVParser;

import java.time.LocalDate;
import java.util.List;

public class FullMoonCalendar {
    private static final List<LocalDate> FULL_MOONS;
    static {
        FULL_MOONS = new CSVParser().getFullMoon();
    }
    public static boolean init(){
        return !FULL_MOONS.isEmpty();
    }
    public static List<LocalDate> getFullMoons() {
        return FULL_MOONS;
    }
}
