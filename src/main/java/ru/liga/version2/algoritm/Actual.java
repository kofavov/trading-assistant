package ru.liga.version2.algoritm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.helper.DateHelper;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.DataTable;
import ru.liga.version2.model.Request;
import ru.liga.version2.parser.CBRFExchange;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class Actual implements Algoritm {
    private final Map<String, Map<LocalDate, Case>> predictionData = new HashMap<>();
    private final Map<String, Map<LocalDate, Case>> data = new HashMap<>(DataTable.getDATA());

    @Override
    public Map<String, Map<LocalDate, Case>> getPrediction(Request request) {
        log.info("Используется алгоритм {}", request.getAlgoritm().getClass().getSimpleName());

        for (String iso : request.getISO_Char_Codes()) {
            predictionData.put(iso, process(data.get(iso), request, iso));
        }
        return predictionData;
    }

    private Map<LocalDate, Case> process(Map<LocalDate, Case> dataOneCur, Request request, String iso) {
        Map<LocalDate, Case> historyData = new TreeMap<>();
        Map<LocalDate, Case> newData = new TreeMap<>();
        LocalDate tempDate = request.getDate();
        LocalDate twoYearsAgoTemp = tempDate.minusYears(2);
        LocalDate threeYearsAgoTemp = tempDate.minusYears(3);
        historyData.putAll(getOldData(twoYearsAgoTemp, request.getStopDay().minusYears(2), dataOneCur, iso));
        historyData.putAll(getOldData(threeYearsAgoTemp, request.getStopDay().minusYears(3), dataOneCur, iso));
        List<Case> historyCase = new ArrayList<>(historyData.values());

        putNewCases(newData, tempDate, historyCase);
        return newData;
    }

    private void putNewCases(Map<LocalDate, Case> newData, LocalDate tempDate, List<Case> historyCase) {
        for (int i = 0; i < historyCase.size() / 2; i++) {
            if (DateHelper.checkWeekend(tempDate)) {
                tempDate = tempDate.plusDays(2);
            }else {
                tempDate = tempDate.plusDays(1);
            }
            Case caseThreeYearsAgo = historyCase.get(i);
            Case caseTwoYearsAgo = historyCase.get(i + historyCase.size() / 2);
            if (caseThreeYearsAgo == null || caseTwoYearsAgo == null) continue;
            double twoYearsAgoVal = caseTwoYearsAgo.getValue();
            double threeYearsAgoVal = caseThreeYearsAgo.getValue();
            double value = twoYearsAgoVal + threeYearsAgoVal;
            Case c = new Case(caseThreeYearsAgo, value, tempDate);
            newData.put(c.getDate(), c);
        }
    }

    private Map<LocalDate, Case> getOldData(LocalDate tempDate, LocalDate stopDay,
                                               Map<LocalDate, Case> dataOneCur, String iso) {
        Map<LocalDate, Case> newData = new TreeMap<>();
        CBRFExchange cbrfExchange = new CBRFExchange();
        while (tempDate.isBefore(stopDay)) {
            if (dataOneCur.containsKey(tempDate)) {
                newData.put(tempDate, dataOneCur.get(tempDate));
            } else if (!DateHelper.checkWeekend(tempDate)) {
                try {
                    newData.put(tempDate, cbrfExchange.getDataForDay(tempDate, iso));
                } catch (Exception ignore) {
                }
            }
            tempDate = tempDate.plusDays(1);
        }
        return newData;
    }
}
