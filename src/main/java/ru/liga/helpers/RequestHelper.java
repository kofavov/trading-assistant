package ru.liga.helpers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.liga.algoritms.Algo;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;
import ru.liga.model.RequestManyCurrency;
import ru.liga.view.Graph;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RequestHelper {
    private final RequestManyCurrency requestManyCurrency;

    public RequestHelper(RequestManyCurrency requestManyCurrency) {
        this.requestManyCurrency = requestManyCurrency;
    }

    public String executeRequest() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (requestManyCurrency.getTypeRequest()) {
            case "/help":
                stringBuilder.append(helpText());
                break;
            case "/currency":
                stringBuilder.append(Currency.getCurrencyMapToString());
                break;
        }
        for (String s : requestManyCurrency.getISO_Char_Codes()) {
            Request request = new Request(requestManyCurrency, s);
            switch (requestManyCurrency.getTypeRequest()) {
                case "/history":
                    try {
                        stringBuilder.append("history for ").append(s).append("\n")
                                .append(getHistoryString(request));
                    }catch (Exception e){
                        stringBuilder.append("Не удалось получить историю по запросу ").append(request).append("\n");
                    }
                    break;
                case "/rate":
                    try {
                        String predictData = getPrediction(request);
                        stringBuilder.append("predict for ").append(s).append("\n").append(predictData);
                    } catch (Exception e) {
                        stringBuilder.append("Не удалось получить данные по запросу ").append(request).append("\n");
                    }
                    break;
            }
        }
        if (stringBuilder.length() == 0) return "что-то не так";
        else return stringBuilder.toString();
    }


    private String getPrediction(Request request) throws Exception {
        List<Case> predictionData = getPredictionData(request);
        StringBuilder stringBuilder = new StringBuilder();
        predictionData.forEach(s -> stringBuilder.append(s).append("\n"));
        return stringBuilder.toString();
    }

    private List<Case> getPredictionData(Request request) throws Exception {
        List<Case> data = DataHelper.getData(request);
        Algo algo = Algo.getAlgo(data, request);
        return Objects.requireNonNull(algo).getPrediction();
    }

    public String helpText() {
        return "trading-assistant\n" +
                "Пример запроса: /rate USD -period week -alg lr\n" +
                "Если хотите увидеть список возможных валют,введите /currency\n" +
                "Если историю history и ISO валюты\n" +
                "Пример запроса: /history USD\n" +
                "Доступные тайм фреймы для прогноза tomorrow, week, month\n" +
                "Доступные алгоритмы: avg, lr, lri, act, moon\n"+
                "Можно узнать прогноз на дату\n" +
                "Пример запроса: /rate USD -date 22.02.2022\n "+
                "Для отображения графика добавить -output graph\n"+
                "Можно получить данные по нескольким валюта\n" +
                "Пример запроса: /rate USD,EUR,TRY -period week -alg lr";
    }

    /**
     * метод выводит исторические данные в консоль
     */
    private static List<Case> getHistory(Request request) throws Exception {
        List<Case> data = DataHelper.getData(request);
        System.out.println(data);
        return Objects.requireNonNull(data).subList(0, Math.min(data.size(), 30));
    }

    private static String getHistoryString(Request request) throws Exception {
        List<Case> data = getHistory(request);
        StringBuilder stringBuilder = new StringBuilder();
        data.forEach(c -> stringBuilder.append(c.toString()).append("\n"));
        return stringBuilder.toString();
    }

    /**
     * Проверка запроса
     * Если введеный ISO код есть в базе доступных валют, то return true
     * Иначе выводится сообщение о том, что такая валюта недоступна или же
     * тайм фрейм введен неправильно
     *
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

    public InputFile executeGraphRequest() throws Exception {
        Graph graph = new Graph();
        for (String s : requestManyCurrency.getISO_Char_Codes()) {
            Request request = new Request(requestManyCurrency, s);
            switch (requestManyCurrency.getTypeRequest()) {
                case "/history": {
                    List<Case> data = getHistory(request);
                    Collections.reverse(data);
                    graph.addData(data, request);
                    break;
                }
                case "/rate": {
                    List<Case> data = getPredictionData(request);
                    graph.addData(data, request);
                    break;
                }
            }
        }
        graph.draw();
        return new InputFile(new File("graph.png"));
    }
}
