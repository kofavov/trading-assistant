package ru.liga.helpers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;
import ru.liga.parsers.Parser;

import java.io.IOException;
import java.util.List;

@Slf4j
public class DataHelper {
    /**
     * Получение исторических данных
     * Если не получается взять данные из ЦБ,
     * то они берутся из csv файла
     * Если и его нет, то выводится сообщение о том что файла не существует
     *
     * @param request запрос пользователя
     * @return List с историческими данными
     * @throws IOException если файла не существует или данные отсутствуют
     */
    public static List<Case> getData(Request request) throws Exception {
        List<Case> data;
        try {
            Parser parser = new CBRFExchange();
            data = parser.getData(request);
            log.info("Данные по запросу {} получены из ЦБ", request.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Данные с сервера ЦБРФ недоступны\n" +
                    "Используются локальные данные");
            Parser parser = new CSVParser();
            data = parser.getData(request);
            log.info("Данные по запросу {} получены из csv файла", request.toString());
        }
        if (data.isEmpty()) throw new IOException("нет данных");
        return data.subList(0, Math.min(data.size(), 10));
    }
}
