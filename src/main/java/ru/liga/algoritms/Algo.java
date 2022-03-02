package ru.liga.algoritms;

import ru.liga.helpers.DateHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Algo {
    protected List<Case> newData;
    protected Request request;
    protected LocalDate lastDayInList;
    protected int countDaysForPredict;
    protected LocalDate stopDay;
    /**
     * @param data    List с полученными данными из какого-либо источника
     * @param request запрос пользователя
     */
    public Algo(List<Case> data, Request request) {
        newData = new ArrayList<>(data);
        this.request = request;
        //последний известный день
        lastDayInList = newData.get(0).getDate();
        //вычисление последнего дня для прогноза
        countDaysForPredict = DateHelper.getCountDays(request);
        stopDay = lastDayInList.plusDays(countDaysForPredict);
        //сб и вс пропускаются поэтому надо добавить еще 2 дня для прогноза на 7 дней
        if (countDaysForPredict == 7) stopDay = stopDay.plusDays(2);
        //если есть завтрашние данные -1 прогнозируемый день
        if (lastDayInList.isAfter(LocalDate.now())){
           stopDay = stopDay.minusDays(1);
        }
    }

    public abstract List<Case> getPrediction();

    public static Algo getAlgo(List<Case> data, Request request) {
        request = RequestHelper.getAlgoRequest(request);
        if (request.getAlgoritm().equals("avg")) {
            return new AverageForTheWeekAlgo(data, request);
        }
        if (request.getAlgoritm().equals("lr")) {
            return new LineRegression(data, request);
        }
        return null;
    }

    protected void addNewCase(List<Case> newData, double newValue) {
        Case currentCase = newData.get(0);
        Case newCase = new Case();
        //заменить DataHelper на +1 если не нужно пропускать сб и вс
        newCase.setDate(currentCase.getDate().plusDays(DateHelper.checkDayOfWeek(currentCase)));
        newCase.setCurrency(currentCase.getCurrency());
        newCase.setValue(newValue);
        newData.add(0, newCase);
        newData.remove(newData.size() - 1);
    }
}
