package ru.liga.version2.algoritm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import ru.liga.version2.model.Case;

import java.util.*;

@Slf4j
public class LineRegression extends SimpleAlgo {

    @Override
    protected double getValue(List<Case> data) {
        SimpleRegression simpleRegression = new SimpleRegression(true);
        //берем последние n значений (в данном случае 7)
        double[][] dataArray = new double[7][2];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i][0] = i;
            dataArray[i][1] = data.get(i).getValue();
        }
        simpleRegression.addData(dataArray);
        //Выбор коэффициента(фактора?) по умолчанию 1.5
        return simpleRegression.predict(0);
    }
}
