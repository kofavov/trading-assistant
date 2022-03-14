package ru.liga.algoritms;

import lombok.extern.slf4j.Slf4j;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
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
        log.info("Используется актуальный алгоритм");
        getNewData();
        return newData;
    }

    private void getNewData() {
        if (newData.size()==1){
            return ;
        }
        List<Case> threeYearsAgoData = newData.subList(0,newData.size()/2);
        List<Case> twoYearsAgoData = newData.subList(newData.size()/2,newData.size());
        newData = new ArrayList<>();

        for (int i = 0, j = 0; newData.size() < threeYearsAgoData.size(); i++,j++) {
            Case c = new Case();
            c.setCurrency(request.getISO_Char_Code());
            LocalDate day = request.getDate().plusDays(j);
            if (DateHelper.checkWeekend(day)){
                i--;
                continue;
            }
            c.setDate(day);
            c.setValue(threeYearsAgoData.get(i).getValue()+twoYearsAgoData.get(i).getValue());
            c.setNominal(threeYearsAgoData.get(i).getNominal());
            newData.add(c);
        }

    }
}
