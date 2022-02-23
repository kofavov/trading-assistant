package ru.liga;

import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageForTheWeekAlgo;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.parsers.CBRFExchange;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        //получаем запрос [0]-rate [1]-какая валюта [2]-таймфрейм
        String[] requestParam = RequestHelper.getRequestParam();
        //получаем данные по валюте
        List<Case> data = CBRFExchange.getData(requestParam);
//        List<Case> data = CSVParser.getData(requestParam);
        //делаем прогноз
        Algo algo = new AverageForTheWeekAlgo();
//        Algo algo = new LineRegression();
        List<Case> prediction = algo.getPrediction(data, requestParam);
        //выводим результат
        //неделя вт-сб как и в csv файле или же из данных цбрф
        //если необходимо пн-пт то в DataHelper изменить дни недели в методе checkDayOfWeek
        prediction.forEach(System.out::println);
    }
}
