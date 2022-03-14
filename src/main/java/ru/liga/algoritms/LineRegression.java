package ru.liga.algoritms;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.Collections;
import java.util.List;
@Slf4j
public class LineRegression extends ru.liga.algoritms.Algo {
    public LineRegression(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        log.info("Используется алгоритм линейной регрессии (SimpleRegression)");
        //добавляем новые значения пока не достигнем целевого дня
        int i = 0;
        while (newData.get(0).getDate().isBefore(stopDay)) {
            double newValue = process();
            addNewCase(newData, newValue);
            i++;
        }
        newData = newData.subList(0, Math.min(i, countDaysForPredict));
        Collections.reverse(newData);
        return newData;
    }

    private double process() {
        SimpleRegression simpleRegression = new SimpleRegression(true);
        //берем последние n значений (в данном случае 7)
        double[][] dataArray = new double[7][2];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i][0] = i;
            dataArray[i][1] = newData.get(i).getValue();
        }
        simpleRegression.addData(dataArray);
        //Выбор коэффициента(фактора?) по умолчанию 1.5
        return simpleRegression.predict(0);
    }
}
