package ru.liga.algoritms;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Request;
import java.util.Collections;
import java.util.List;

/**
 * Класс позволяет получить среднее значение курса на базе предыдущих значений за неделю
 * Если данные устарели, то прогноз выполняется и на отсутствующие числа
 */
@Slf4j
public class AverageForTheWeekAlgo extends ru.liga.algoritms.Algo {
    public AverageForTheWeekAlgo(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        log.info("Используется алгоритм среднего значения за неделю");
        int i = getNewData();
        newData = newData.subList(0, Math.min(i, countDaysForPredict));
        Collections.reverse(newData);
        return newData;
    }

    private int getNewData() {
        int i = 0;
        while (newData.get(0).getDate().isBefore(stopDay)) {
            //получаю среднее значение за 7 дней, если добавляется новый день, то он включается в список для вычисления
            double avg = newData.subList(0, 7).stream().mapToDouble(Case::getValue).average().getAsDouble();
            addNewCase(newData, avg);
            i++;
        }
        return i;
    }
}
