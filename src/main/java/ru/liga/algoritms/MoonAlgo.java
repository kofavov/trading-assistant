package ru.liga.algoritms;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Slf4j
public class MoonAlgo extends Algo{
    /**
     * @param data    List с полученными данными из какого-либо источника
     * @param request запрос пользователя
     */
    public MoonAlgo(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        log.info("Используется мистический алгоритм");
        addFirstElement();
        while (newData.get(0).getDate().isBefore(stopDay)) {
            double random = (((Math.random()*20) - 10)/100)+1;
            addNewCase(newData, newData.get(0).getValue()*random);
        }
        newData = newData.subList(0,countDaysForPredict);
        Collections.reverse(newData);
        return newData;
    }

    private void addFirstElement() {
        double firstResult = newData.stream().mapToDouble(Case::getValue).average().getAsDouble();
        Case c = new Case();
        c.setCurrency(newData.get(0).getCurrency());
        c.setValue(firstResult);
        c.setDate(request.getDate());
        c.setNominal(newData.get(0).getNominal());
        newData.add(0,c);
    }
}
