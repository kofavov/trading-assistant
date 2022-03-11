package ru.liga.helpers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
//        try {
//            Parser parser = new CBRFExchange();
//            data = parser.getData(request);
//            log.info("Данные по запросу {} получены из ЦБ", request.toString());
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println("Данные с сервера ЦБРФ недоступны\n" +
//                    "Используются локальные данные");
//            Parser parser = new CSVParser();
//            data = parser.getData(request);
//            log.info("Данные по запросу {} получены из csv файла", request.toString());
//        }
        CSVParser csvParser = new CSVParser();
        data = csvParser.getData(request);
        List<Case> newCases = csvParser.getNewData(request);
        boolean emptyLists = data.isEmpty() && newCases.isEmpty();
//        if (emptyLists) {
//            boolean dateBeforeNow = (data.get(0).getDate().isBefore(LocalDate.now())
//                && newCases.get(0).getDate().isBefore(LocalDate.now()));
//            if (dateBeforeNow) {
//                CBRFExchange cbrfExchange = new CBRFExchange();
//                int missDay = data.isEmpty() ? 25
//                        : (int) ChronoUnit.DAYS.between(data.get(0).getDate(), LocalDate.now());
//                newCases = cbrfExchange.getData(request, missDay);
//                csvParser.saveData(newCases, request);
//            }
//        }
        if (emptyLists) {
            int missDay = 25;
            CBRFExchange cbrfExchange = new CBRFExchange();
            newCases = cbrfExchange.getData(request, missDay);
            csvParser.saveData(newCases, request);
        }
        if (!newCases.isEmpty() && newCases.get(0).getDate().isBefore(LocalDate.now())) {
            int missDay = data.isEmpty() ? 25
                    : (int) ChronoUnit.DAYS.between(data.get(0).getDate(), LocalDate.now());
            CBRFExchange cbrfExchange = new CBRFExchange();
            newCases = cbrfExchange.getData(request, missDay);
            csvParser.saveData(newCases, request);
        }
        if (newCases.isEmpty() && !data.isEmpty() && data.get(0).getDate().isBefore(LocalDate.now())) {
            int missDay = data.isEmpty() ? 25
                    : (int) ChronoUnit.DAYS.between(data.get(0).getDate(), LocalDate.now());
            CBRFExchange cbrfExchange = new CBRFExchange();
            newCases = cbrfExchange.getData(request, missDay);
            csvParser.saveData(newCases, request);
        }

        data.addAll(0, newCases);
        if (data.isEmpty()) throw new IOException("нет данных");
        data.get(0).setCurrency(request.getISO_Char_Code());
        return data;
    }
}
