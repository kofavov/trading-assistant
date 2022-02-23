package ru.liga.algoritms;

import org.apache.commons.math3.util.Precision;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;

import java.util.List;

public interface Algo {
    List<Case> getPrediction(List<Case> data, String[] request);

    default void setFuturePoint(List<Case> data, double newValue) {
        Case currentPoint = data.get(0);
        Case futurePoint = new Case();
        //заменить на +1 если не нужно пропускать сб и вс
        futurePoint.setDate(currentPoint.getDate().plusDays(DateHelper.checkDayOfWeek(currentPoint)));
        futurePoint.setCurrency(currentPoint.getCurrency());
        futurePoint.setValue(Precision.round(newValue, 4));
        data.add(0, futurePoint);
        data.remove(data.size() - 1);
    }
}
