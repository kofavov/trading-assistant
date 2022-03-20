package ru.liga.version2.algoritm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.helper.DateHelper;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.DataTable;
import ru.liga.version2.model.Request;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public abstract class SimpleAlgo implements Algoritm {
    protected final Map<String, Map<LocalDate, Case>> predictionData = new HashMap<>();
    protected final Map<String, Map<LocalDate, Case>> data = new HashMap<>(DataTable.getDATA());

    public Map<String, Map<LocalDate, Case>> getPrediction(Request request) {
        log.info("Используется алгоритм {}", request.getAlgoritm().getClass().getSimpleName());
        for (String iso : request.getISO_Char_Codes()) {
            predictionData.put(iso, process(data.get(iso), request));
        }
        return predictionData;
    }

    protected Map<LocalDate, Case> process(Map<LocalDate, Case> data, Request request) {
        LocalDate tempDay = data.keySet().stream().max(LocalDate::compareTo).orElseThrow();
        List<Case> newAndOldCases = new ArrayList<>(data.values());
        Map<LocalDate, Case> newCases = new TreeMap<>();
        Collections.reverse(newAndOldCases);
        LocalDate stopDay = request.getStopDay();
        //возможно есть данные на завтра
        if (tempDay.isAfter(LocalDate.now())) {
            newCases.put(tempDay, data.get(tempDay));
        }
        putNewCases(tempDay, newAndOldCases, newCases, stopDay);
        return newCases;
    }

    private void putNewCases(LocalDate tempDay, List<Case> newAndOldCases, Map<LocalDate, Case> newCases, LocalDate stopDay) {
        while (tempDay.isBefore(stopDay)) {
            tempDay = tempDay.plusDays(1);
            if (DateHelper.checkWeekend(tempDay)) {
                tempDay = tempDay.plusDays(2);
            }
            Case oldCase = newAndOldCases.get(0);
            double value = getValue(newAndOldCases);
            Case newCase = new Case(oldCase, value, tempDay);
            newAndOldCases.add(0, newCase);
            newCases.put(newCase.getDate(), newCase);
        }
    }

    protected abstract double getValue(List<Case> data);
}
