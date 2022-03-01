package ru.liga.algoritms;

import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс позволяет получить среднее значение курса на базе предыдущих значений за неделю
 * Если данные устарели, то прогноз выполняется и на отсутствующие числа
 */
public class AverageForTheWeekAlgo extends ru.liga.algoritms.Algo {
    public AverageForTheWeekAlgo(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        getNewData();
        newData = newData.subList(0, countDaysForPredict);
        Collections.reverse(newData);
        return newData;
    }

    private void getNewData() {
        while (newData.get(0).getDate().isBefore(stopDay)) {
            //получаю среднее значение за 7 дней, если добавляется новый день, то он включается в список для вычисления
            double avg = newData.subList(0, 7).stream().mapToDouble(Case::getValue).average().getAsDouble();
            addNewCase(newData, avg);
        }
    }
}
