package ru.liga;

import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageSevenDays;
import ru.liga.algoritms.LineRegression;
import ru.liga.helpers.DateHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.parsers.OnlineCBRExchange;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        //получаем запрос
        String[] requestParam = RequestHelper.getRequestParam();
        //получаем данные по валюте
        List<Case> data = OnlineCBRExchange.getData(requestParam);
//        List<Case> data = CSVParser.getData(requestParam);
        //делаем прогноз
//        Algo algo = new AverageSevenDays();
        Algo algo = new LineRegression();
        List<Case> prediction = algo.getPrediction(data, requestParam);
        //выводим результат
        prediction.forEach(System.out::println);
    }
}
