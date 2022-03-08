package ru.liga;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.algoritms.Algo;
import ru.liga.helpers.DataHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.telegram.Bot;

import java.util.List;


public class Main {
    /**
     * выполняем пока не введут exit
     * <p>
     * неделя вт-сб как в csv файле и в данных ЦБРФ
     * если необходимо пн-пт то в DataHelper изменить дни недели в методе checkDayOfWeek,
     * eсли нужно выводить все дни подряд, то там же закомментировать 19-20 строчки
     */


    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
//        RequestHelper requestHelper = new RequestHelper();
//        requestHelper.helpText();
//        Request request;
//        do {
//            //получаем запрос
//            request = requestHelper.getRequestForPrediction();
//            if (request.isExit()) break;
//            //получаем данные по валюте
//            List<Case> data;
//            try {
//                data = DataHelper.getData(request);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//                //если файла нет или еще какая-то ошибка повторяем
//                continue;
//            }
//            //делаем прогноз
//            Algo algo = Algo.getAlgo(data,request);
//            List<Case> prediction = null;
//            if (algo != null) {
//                prediction = algo.getPrediction();
//                //выводим результат
//                prediction.forEach(System.out::println);
//            }
//        } while ((!request.isExit()));
    }
}
