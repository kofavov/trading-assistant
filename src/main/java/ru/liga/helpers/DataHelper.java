package ru.liga.helpers;

import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;

import java.io.IOException;
import java.util.List;

public class DataHelper {
    public static List<Case> getData(Request request) throws IOException {
        List<Case> data;
        try {
            data = CBRFExchange.getData(request);
        } catch (Exception e) {
            System.out.println("Данные с сервера ЦБРФ недоступны\n" +
                    "Используются локальные данные");
            data = CSVParser.getData(request);
        }
        return data;
    }
}
