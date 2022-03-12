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
import java.util.ArrayList;
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
//    public static List<Case> getData(Request request) throws Exception {
//        List<Case> data;
//        CSVParser csvParser = new CSVParser();
//        data = csvParser.getData(request);
//        List<Case> newCases = csvParser.getNewData(request);
//        boolean emptyLists = data.isEmpty() && newCases.isEmpty();
//        if (emptyLists) {
//            int missDay = 50;
//            CBRFExchange cbrfExchange = new CBRFExchange();
//            newCases = cbrfExchange.getData(request, missDay);
//            csvParser.saveData(newCases, request);
//        } else if (!newCases.isEmpty() && newCases.get(0).getDate().isBefore(LocalDate.now()) &&
//                !DateHelper.checkWeekend(LocalDate.now().plusDays(1))) {
//            int missDay = (int) ChronoUnit.DAYS.between(newCases.get(0).getDate(), LocalDate.now());
//            CBRFExchange cbrfExchange = new CBRFExchange();
//            newCases = cbrfExchange.getData(request, missDay);
//            csvParser.saveData(newCases, request);
//        } else if (newCases.isEmpty() && !data.isEmpty() && data.get(0).getDate().isBefore(LocalDate.now())
//                && !DateHelper.checkWeekend(LocalDate.now().plusDays(1))) {
//            int missDay = (int) ChronoUnit.DAYS.between(data.get(0).getDate(), LocalDate.now());
//            CBRFExchange cbrfExchange = new CBRFExchange();
//            newCases = cbrfExchange.getData(request, missDay);
//            csvParser.saveData(newCases, request);
//        }
//
//        data.addAll(0, newCases);
//        if (data.isEmpty()) throw new IOException("нет данных");
//        data.get(0).setCurrency(request.getISO_Char_Code());
//        return data;
//    }
    public static List<Case> getData(Request request) throws Exception {
        if (request.getAlgoritm().equals("act")){
            return getDataForActAlgo(request);
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
        CBRFExchange cbrfExchange = new CBRFExchange();
        List<Case> cases = new ArrayList<>();
        cbrfExchange.getDataForActualAlgo(cases, request, Currency.getCurrencyMap().get(request.getISO_Char_Code()));
        return cases;
    }
}
