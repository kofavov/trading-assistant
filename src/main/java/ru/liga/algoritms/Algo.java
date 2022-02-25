package ru.liga.algoritms;

import org.apache.commons.math3.util.Precision;
import ru.liga.helpers.DateHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.List;

public abstract class Algo {
    public abstract List<Case> getPrediction(List<Case> data, Request request);

    public static Algo getAlgo(Request request){
        request = RequestHelper.getAlgoRequest(request);
        if (request.getAlgoritm().equals("avg")){
            return new AverageForTheWeekAlgo();
        }
        if (request.getAlgoritm().equals("lr")){
            return new LineRegression();
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
