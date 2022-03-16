package ru.liga.version2.algoritm;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.helper.DateHelper;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.DataTable;
import ru.liga.version2.model.FullMoonCalendar;
import ru.liga.version2.model.Request;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

@Slf4j
public class Moon implements Algoritm {
    private final HashMap<String, TreeMap<LocalDate, Case>> predictionData = new HashMap<>();
    private final HashMap<String, TreeMap<LocalDate, Case>> data = new HashMap<>(DataTable.getDATA());

    @Override
    public HashMap<String, TreeMap<LocalDate, Case>> getPrediction(Request request) {
        log.info("Используется алгоритм {}", request.getAlgoritm().getClass().getSimpleName());
        for (String iso : request.getISO_Char_Codes()) {
            predictionData.put(iso, process(data.get(iso), request));
        }
        return predictionData;
    }

    private TreeMap<LocalDate, Case> process(TreeMap<LocalDate, Case> data, Request request) {
        TreeMap<LocalDate, Case> prediction = new TreeMap<>();
        Case firstElement = getFirstElement(data, request);
        prediction.put(firstElement.getDate(), firstElement);
        Case oldCase;
        LocalDate tempDay = firstElement.getDate();
        while (tempDay.isBefore(request.getStopDay())) {
            oldCase = prediction.get(tempDay);
            tempDay = tempDay.plusDays(1);
            if (DateHelper.checkWeekend(tempDay)) {
                tempDay = tempDay.plusDays(2);
            }
            double random = (((Math.random() * 20) - 10) / 100) + 1;
            double newVal = random * oldCase.getValue();
            Case newCase = new Case(oldCase, newVal, tempDay);
            prediction.put(tempDay,newCase);
        }
        return prediction;
    }

    private Case getFirstElement(TreeMap<LocalDate, Case> data, Request request) {
        TreeMap<LocalDate, Case> dataForFirstCase = getDataForFirstCase(data, request);
        AtomicDouble sum = new AtomicDouble();
        dataForFirstCase.values().forEach(e -> sum.getAndAdd(e.getValue()));
        double val = sum.get() / dataForFirstCase.size();
        Case oldCase = dataForFirstCase.values().stream().findFirst().orElseThrow();
        return new Case(oldCase, val, request.getDate().plusDays(1));
    }

    private TreeMap<LocalDate, Case> getDataForFirstCase(TreeMap<LocalDate, Case> data, Request request) {
        TreeMap<LocalDate, Case> dataForFirstCase = new TreeMap<>();
        Stack<LocalDate> fullMoonStack = getFullMoonStack(request);
        for (int j = 0; j < 3; j++) {
            LocalDate mostNearDay = fullMoonStack.pop();
            Case mostNearCase = null;
            for (int i = 0; i < 7; i++) {
                mostNearCase = data.get(mostNearDay);
                if (mostNearCase != null) break;
                mostNearDay = mostNearDay.minusDays(1);
            }
            if (mostNearCase != null)
                dataForFirstCase.put(mostNearCase.getDate(), mostNearCase);
        }
        return dataForFirstCase;
    }

    private Stack<LocalDate> getFullMoonStack(Request request) {
        Stack<LocalDate> fullMoonStack = new Stack<>();
        List<LocalDate> allFullMoons = FullMoonCalendar.getFullMoons();
        for (LocalDate fm : allFullMoons) {
            if (fm.isBefore(request.getDate())) {
                fullMoonStack.add(fm);
            }
        }
        return fullMoonStack;
    }
}
