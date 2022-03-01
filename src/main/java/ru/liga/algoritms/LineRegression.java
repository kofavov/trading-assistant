package ru.liga.algoritms;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.Collections;
import java.util.List;

public class LineRegression extends ru.liga.algoritms.Algo {
    public LineRegression(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        //добавляем новые значения пока не достигнем целевого дня
        while (newData.get(0).getDate().isBefore(stopDay)) {
            double newValue = process();
            addNewCase(newData, newValue);
        }
        newData = newData.subList(0, countDaysForPredict);
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
