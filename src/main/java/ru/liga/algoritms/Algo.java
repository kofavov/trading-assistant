package ru.liga.algoritms;

import org.apache.commons.math3.util.Precision;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.List;

public abstract class Algo {
    public abstract List<Case> getPrediction(List<Case> data, Request request);

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
