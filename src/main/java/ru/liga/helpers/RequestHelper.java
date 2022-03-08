package ru.liga.helpers;

import lombok.SneakyThrows;
import ru.liga.algoritms.Algo;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RequestHelper {
    public String executeRequest(Request request) throws Exception{
        switch (request.getTypeRequest()){
            case "/help":return helpText();
            case "/currency":return Currency.getCurrencyMapToString();
            case "/history":return getHistory(request);
            case "/rate":return getPrediction(request);
        }
        return "что-то не так";
    }

//    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Метод считывает ввод пользователя и на его основе обращается к другим методам
     * Если ввели exit программа завершается
     * @return Request - запрос пользователя
     */
//    public static Request getRequestForPrediction() {
//        Scanner scanner = new Scanner(System.in);
//        Request request = null;
//        String[] param;
//        System.out.println("Введите запрос");
//        String inputString = scanner.nextLine();
//
//        if (inputString.equals("currency")) {
//            Currency.getCurrencyMap().values().forEach(System.out::println);
//            request = getRequestForPrediction();
//        } else if (inputString.contains("history")) {
//            getHistory(inputString);
//            request = getRequestForPrediction();
//        } else if (inputString.equals("exit")) {
//            request = new Request(true);
//        } else {
//            request = getRequestForPrediction(inputString);
//        }
//        return request;
//    }
    @SneakyThrows
    private String getPrediction(Request request){
        List<Case> data = DataHelper.getData(request);
        Algo algo = Algo.getAlgo(data,request);
        List<Case> predictionData = algo.getPrediction();
        StringBuilder stringBuilder = new StringBuilder();
        predictionData.forEach(s->stringBuilder.append(s).append("\n"));
        return stringBuilder.toString();
    }

    public static Request getAlgoRequest(Request request){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите алгоритм");
        System.out.println("avg или lr");
        try {
            request.setAlgoritm(scanner.nextLine());
        }catch (Exception e){
            System.out.println(e.getMessage());
            request = getAlgoRequest(request);
        }
        return request;
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
     * создает объект типа Request
//     * @param inputString строка введенная пользователем
     * @return запрос
     */
    private  Request getRequestForPrediction(Request request) {

        try {
            if (!checkRequest(request)) {
                throw new IllegalArgumentException("Введите верный запрос");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            request = getRequestForPrediction(request);
        }
        return request;
    }

    /**
     * метод выводит исторические данные в консоль
     *
     */
    private static String getHistory(Request request) {
        List<Case> data = null;
        try {
            data = DataHelper.getData(request);
            System.out.println(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data.toString();
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
}
