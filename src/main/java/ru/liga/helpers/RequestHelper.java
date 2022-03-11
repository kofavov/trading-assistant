package ru.liga.helpers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.liga.algoritms.Algo;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;
import ru.liga.view.Graph;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RequestHelper {
    public String executeRequest(Request request) throws Exception{
        switch (request.getTypeRequest()){
            case "/help":return helpText();
            case "/currency":return Currency.getCurrencyMapToString();
            case "/history":return getHistory(request).toString();
            case "/rate":return getPrediction(request);
        }
        return "что-то не так";
    }

    @SneakyThrows
    private String getPrediction(Request request){
        List<Case> predictionData = getPredictionData(request);
        StringBuilder stringBuilder = new StringBuilder();
        predictionData.forEach(s->stringBuilder.append(s).append("\n"));
        return stringBuilder.toString();
    }

    private List<Case> getPredictionData(Request request) throws Exception {
        List<Case> data = DataHelper.getData(request);
        Algo algo = Algo.getAlgo(data, request);
        return algo.getPrediction();
    }

    public String helpText() {
        return "trading-assistant\n"+
        "Пример запроса: rate USD week\n"+
        "Если хотите увидеть список возможных валют,введите currency\n"+
        "Если историю history и ISO валюты\n"+
        "Пример запроса: history USD\n"+
        "Доступные тайм фреймы tomorrow, week\n"+
        "Для выхода введите exit";
    }

    /**
     * метод выводит исторические данные в консоль
     *
     */
    private static List<Case> getHistory(Request request) {
        List<Case> data = null;
        try {
            data = DataHelper.getData(request);
            System.out.println(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Objects.requireNonNull(data).subList(0, Math.min(data.size(), 30));
    }
    /**
     * Проверка запроса
     * Если введеный ISO код есть в базе доступных валют, то return true
     * Иначе выводится сообщение о том, что такая валюта недоступна или же
     * тайм фрейм введен неправильно
     * @param request запрос
     * @return результат проверки
     */

    private boolean checkRequest(Request request) {
        boolean currency = Currency.getCurrencyMap().containsKey(request.getISO_Char_Code());
        boolean period = request.getPeriod().matches("week|tomorrow");//|month
        if (!currency) {
            System.out.println("невозможно получить информацию по этой валюте");
        }
        if (!period) {
            System.out.println("невозможно получить прогноз на данный период");
        }
        return currency && period;
    }

    @SneakyThrows
    public InputFile executeGraphRequest(Request request) {
        Graph graph = new Graph();
        switch (request.getTypeRequest()){
            case "/history":{
                List<Case> data = getHistory(request);
                Collections.reverse(data);
                graph.draw(data,request);
                break;
            }
            case "/rate":{
                List<Case> data = getPredictionData(request);
                graph.draw(data,request);
                break;
            }
        }
        return new InputFile(new File("graph.png"));
    }
}
