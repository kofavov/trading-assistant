package ru.liga.helpers;

import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.NewCBRFExchange;
import ru.liga.parsers.CSVParser;
import ru.liga.parsers.NewCBRFExchange;

import java.io.IOException;
import java.util.List;

public class DataHelper {
    /**
     * Получение исторических данных
     * Если не получается взять данные из ЦБ,
     * то они берутся из csv файла
     * Если и его нет, то выводится сообщение о том что файла не существует
     * @param request запрос пользователя
     * @return List с историческими данными
     * @throws IOException если файла не существует или данные отсутствуют
     */
    public static List<Case> getData(Request request) throws IOException {
        List<Case> data;
        try {
            data = NewCBRFExchange.getData(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Данные с сервера ЦБРФ недоступны\n" +
                    "Используются локальные данные");
            data = CSVParser.getData(request);
        }
        if (data.isEmpty())throw new IOException("нет данных");
        return data;
    }
}
