package ru.liga;

import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageForTheWeekAlgo;
import ru.liga.helpers.DataHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.util.List;


public class Main {
    /**
     * выполняем пока не введут exit
     * <p>
     * неделя вт-сб как в csv файле и в данных ЦБРФ
     * если необходимо пн-пт то в DataHelper изменить дни недели в методе checkDayOfWeek,
     * eсли нужно выводить все дни подряд, то там же закомментировать 19-20 строчки
     */

    private static boolean exit = false;

    public static void main(String[] args) {
        while (!exit) {
            //получаем запрос
            Request request = RequestHelper.getRequestForPrediction();
            //получаем данные по валюте
            List<Case> data;
            try {
                data = DataHelper.getData(request);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //если файла нет или еще какая-то ошибка повторяем
                continue;
            }
            //делаем прогноз
            Algo algo = new AverageForTheWeekAlgo();
//        Algo algo = new LineRegression();
            List<Case> prediction = algo.getPrediction(data, request);
            //выводим результат
            prediction.forEach(System.out::println);
        }
    }
}
