package ru.liga.algoritms;

import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Actual extends Algo{
    /**
     * @param data    List с полученными данными из какого-либо источника
     * @param request запрос пользователя
     */
    public Actual(List<Case> data, Request request) {
        super(data, request);
    }

    @Override
    public List<Case> getPrediction() {
        getNewData();
        Collections.reverse(newData);
        return newData;
    }

    private void getNewData() {
        List<Case> threeYearsAgoData = newData.subList(0,newData.size()/2);
        List<Case> twoYearsAgoData = newData.subList(newData.size()/2,newData.size());
        newData = new ArrayList<>();
        for (int i = 0; i < threeYearsAgoData.size(); i++) {
            Case c = new Case();
            c.setCurrency(request.getISO_Char_Code());
            c.setDate(request.getDate().plusDays(i));
            c.setValue(threeYearsAgoData.get(i).getValue()+twoYearsAgoData.get(i).getValue());
            c.setNominal(threeYearsAgoData.get(i).getNominal());
            newData.add(c);
        }
        Collections.reverse(newData);
    }
}
