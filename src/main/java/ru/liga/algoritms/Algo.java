package ru.liga.algoritms;

import org.apache.commons.math3.util.Precision;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;

import java.util.List;

public abstract class Algo {
    public abstract List<Case> getPrediction(List<Case> data, String[] request);

    protected void addNewCase(List<Case> newData, double newValue) {
        Case currentCase = newData.get(0);
        Case newCase = new Case();
        //заменить DataHelper на +1 если не нужно пропускать сб и вс
        newCase.setDate(currentCase.getDate().plusDays(DateHelper.checkDayOfWeek(currentCase)));
        newCase.setCurrency(currentCase.getCurrency());
        //округление до n знаков после запятой
        newCase.setValue(Precision.round(newValue, 2));
        newData.add(0, newCase);
        newData.remove(newData.size() - 1);
    }
}
