package ru.liga.version2.helper;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.liga.version2.exception.RequestException;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.DataTable;
import ru.liga.version2.model.Request;
import ru.liga.version2.view.Graph;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class RequestHelper {
    private final Request request;

    public RequestHelper(Request request) {
        this.request = request;
    }

    public String executeTextRequest() {
        switch (request.getTypeRequest()) {
            case ("/help"):
                return helpText();
            case ("/currency"):
                return DataTable.getCurrenciesToString();
            case ("/history"):
                return DataTable.getHistoryToString(request);
            case ("/rate"):
                return getPredictionToString(request);
            default:
                return "неверный тип запроса";
        }
    }

    public InputFile executeGraphRequest() throws Exception {
        HashMap<String, TreeMap<LocalDate, Case>> data = new HashMap<>();

        switch (request.getTypeRequest()) {
            case "/history": {
                for (String iso : request.getISO_Char_Codes()) {
                    TreeMap<LocalDate, Case> cur = new TreeMap<>(DataTable.getDATA().get(iso)
                            .subMap(LocalDate.now().minusMonths(3), true,
                                    LocalDate.now(), true));

                    data.put(iso, cur);
                }
                break;
            }

            case "/rate": {
                data.putAll(getPrediction(request));
                break;
            }
            default:throw new RequestException("Неверный запрос");
        }

        Graph graph = new Graph(data);
        graph.draw();
        return new InputFile(new File("graph.png"));
    }


    private String getPredictionToString(Request request) {
        HashMap<String, TreeMap<LocalDate, Case>> predictionData = getPrediction(request);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, TreeMap<LocalDate, Case>> predict : predictionData.entrySet()) {
            stringBuilder.append("Predict for ").append(predict.getKey()).append("\n");
            TreeMap<LocalDate, Case> onePredictData = new TreeMap<>(predictionData.get(predict.getKey()));
            List<Case> cases = new ArrayList<>(onePredictData.values());
            for (Case c : cases) {
                stringBuilder.append(c).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private HashMap<String, TreeMap<LocalDate, Case>> getPrediction(Request request) {
        HashMap<String, TreeMap<LocalDate, Case>> fullPrediction = request.getAlgoritm().getPrediction(request);
        HashMap<String, TreeMap<LocalDate, Case>> cutPrediction = new HashMap<>();
        for (Map.Entry<String, TreeMap<LocalDate, Case>> cur : fullPrediction.entrySet()) {
            TreeMap<LocalDate, Case> cutCases = new TreeMap<>();
            for (Map.Entry<LocalDate, Case> c : cur.getValue().entrySet()) {
                LocalDate startDay = request.getDate().minusDays(1);
                if (c.getKey().isAfter(startDay)) {
                    cutCases.put(c.getKey(), c.getValue());
                }
            }
            cutPrediction.put(cur.getKey(), cutCases);
        }
        return cutPrediction;
    }

    private String helpText() {
        return "trading-assistant\n" +
                "Пример запроса: /rate USD -period week -alg lr\n" +
                "Если хотите увидеть список возможных валют,введите /currency\n" +
                "Если историю history и ISO валюты\n" +
                "Пример запроса: /history USD\n" +
                "Доступные тайм фреймы для прогноза tomorrow, week, month\n" +
                "Доступные алгоритмы: avg, lr, lri, act, moon\n" +
                "Можно узнать прогноз на дату\n" +
                "Пример запроса: /rate USD -date 22.02.2022\n " +
                "Для отображения графика добавить -output graph\n" +
                "Можно получить данные по нескольким валюта\n" +
                "Пример запроса: /rate USD,EUR,TRY -period week -alg lr";
    }
}
