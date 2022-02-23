package ru.liga.algoritms;

import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AverageSevenDays implements Algo {
    @Override
    public List<Case> getPrediction(List<Case> data, String[] request) {
        //создаю новый лист чтобы не вносить изменения в предыдущий
        List<Case> newData = new ArrayList<>(data);
        LocalDate today = LocalDate.now();
        LocalDate lastDayInList = newData.get(0).getDate();//последний известный день
        int countDaysForPredict = DateHelper.getCountDays(request);

        //добавляю количество дней до сегодня + количество дней для прогноза
        int plusDays = countDaysForPredict + (int) ChronoUnit.DAYS.between(lastDayInList, today);
        LocalDate stopDay = lastDayInList.plusDays(plusDays);
        //если только от последнего известного дня
//        LocalDate stopDay = lastDayInList.plusDays(countDaysForPredict);

        //сб и вс пропускаются поэтому надо добавить еще 2 дня для прогноза
        if (countDaysForPredict == 7) stopDay = stopDay.plusDays(2);

        getNewData(newData, stopDay);
        newData = newData.subList(0, countDaysForPredict);
        Collections.reverse(newData);
        return newData;
    }

    private void getNewData(List<Case> newData, LocalDate stopDay) {
        while (!newData.get(0).getDate().equals(stopDay)) {
            //получаю среднее значение за 7 дней, если добавляется новый день, то он включается в список для вычисления
            double avg = newData.subList(0, 7).stream().mapToDouble(Case::getValue).average().getAsDouble();
            Algo.setFuturePoint(newData,avg);
        }
    }
}
