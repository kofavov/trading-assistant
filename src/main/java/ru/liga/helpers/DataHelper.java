package ru.liga.helpers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        if (request.getAlgoritm().equals("act")) {
            return getDataForActAlgo(request);
        }
        if (request.getAlgoritm().equals("moon")) {
            return getDataForMoonAlgo(request);
        }
        CSVParser csvParser = new CSVParser();
        List<Case> data = csvParser.getNewData(request);//csv файл из папки tempRate (ЦБ + данные из задания)
        if (data.isEmpty() || data.size() < 30) {
            data.addAll(csvParser.getOldData(request));//csv файл из общей папки с данными из задания
        }
        //если нет данных или они устарели, берем из ЦБ
        data = getCBRFData(request, csvParser, data);
        if (data.isEmpty()) {
            log.info("нет данных");
            throw new IOException("нет данных");
        }
        data.get(0).setCurrency(request.getISO_Char_Code());
        return data;
    }

    private static List<Case> getDataForMoonAlgo(Request request) {
        LinkedHashSet<LocalDate> fullMoonCalendar = CSVParser.getFullMoonSet();
        Stack<LocalDate> fullMoonStack = new Stack<>();
        for (LocalDate date : fullMoonCalendar) {
            if (date.isAfter(request.getDate())) break;
            fullMoonStack.push(date);
        }
        LinkedList<Case> casesFullMoon = new LinkedList<>();
        CSVParser csvParser = new CSVParser();
        List<Case> data = csvParser.getNewData(request);//csv файл из папки tempRate (ЦБ + данные из задания)
        if (data.isEmpty() || data.size() < 30) {
            data.addAll(csvParser.getOldData(request));//csv файл из общей папки с данными из задания
        }
        for (int i = 0; i < 3; i++) {
            Optional<Case> fullMoonCase = data.stream().filter(e -> e.getDate().isBefore(fullMoonStack.peek().plusDays(1))).findFirst();
            if (fullMoonCase.isPresent()) {
                casesFullMoon.add(fullMoonCase.get());
                fullMoonStack.pop();
            }
        }

        if (casesFullMoon.size() < 3) {
            for (int i = 0; i <= 3 - casesFullMoon.size(); i++) {
                CBRFExchange cbrfExchange = new CBRFExchange();
                Optional<Case> o = cbrfExchange.getDataForDay(fullMoonStack.pop(),
                        Currency.getCurrencyMap().get(request.getISO_Char_Code()));
                o.ifPresent(casesFullMoon::add);
            }
        }

        return casesFullMoon;
    }

    private static List<Case> getCBRFData(Request request, CSVParser csvParser, List<Case> data) {
        try {
            if (data.isEmpty()) {
                int missDay = 50;
                CBRFExchange cbrfExchange = new CBRFExchange();
                data = cbrfExchange.getData(request, missDay);
                csvParser.saveData(data, request);
            } else if (data.get(0).getDate().isBefore(LocalDate.now())) {
                int missDay = (int) ChronoUnit.DAYS.between(data.get(0).getDate(), LocalDate.now());
                //если сегодня сб или вс нет смысла проверять данные
                if (DateHelper.checkDayOfWeek(data.get(0)) == 1 || missDay > 2) {
                    CBRFExchange cbrfExchange = new CBRFExchange();
                    List<Case> newCases = cbrfExchange.getData(request, missDay);
                    if (!newCases.isEmpty()) {
                        data.addAll(0, newCases);
                        csvParser.saveData(data, request);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Новых данных нет");
        }
        return data;
    }

    private static List<Case> getDataForActAlgo(Request request) {
        CSVParser csvParser = new CSVParser();
        CBRFExchange cbrfExchange = new CBRFExchange();
        List<Case> data = new ArrayList<>(csvParser.getActAlgoData(request));

        if (data.isEmpty()) {
            cbrfExchange.getDataForActualAlgo(data, request, Currency.getCurrencyMap().get(request.getISO_Char_Code()));
        }
        return data;
    }
}
