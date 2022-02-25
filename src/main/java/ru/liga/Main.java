package ru.liga;

import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageForTheWeekAlgo;
import ru.liga.algoritms.LineRegression;
import ru.liga.helpers.DataHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CBRFExchange;
import ru.liga.parsers.CSVParser;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        //выполняем пока не введут exit
        while (true) {
            //получаем запрос
            Request request = RequestHelper.getRequest();
            //получаем данные по валюте
            List<Case> data;
            try {
                data = DataHelper.getData(request);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //если файла нет или еще какая-то ошибка повторяем
                main(new String[0]);
                return;
            }
            //делаем прогноз
            Algo algo = new AverageForTheWeekAlgo();
//        Algo algo = new LineRegression();
            List<Case> prediction = algo.getPrediction(data, request);
            //выводим результат
            //неделя вт-сб как и в csv файле или же из данных цбрф
            //если необходимо пн-пт то в DataHelper изменить дни недели в методе checkDayOfWeek
            prediction.forEach(System.out::println);
        }
    }
}
