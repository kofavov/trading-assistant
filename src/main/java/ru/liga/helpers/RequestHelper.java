package ru.liga.helpers;

import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RequestHelper {
    /**
     * Метод считывает ввод пользователя и на его основе обращается к другим методам
     * Если ввели exit программа завершается
     * @return Request - запрос пользователя
     */
    public static Request getRequestForPrediction() {
        Request request = null;
        String[] param;

        helpText();
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();

        if (inputString.equals("currency")) {
            Currency.getCurrencyMap().values().forEach(System.out::println);
            request = getRequestForPrediction();
        } else if (inputString.contains("history")) {
            getHistory(inputString);
            request = getRequestForPrediction();
        } else if (inputString.equals("exit")) {
            request = new Request(true);
        } else {
            request = getRequestForPrediction(inputString);
        }
        return request;
    }

    private static void helpText() {
        System.out.println("Введите запрос");
        System.out.println("Пример: rate USD week");
        System.out.println("Если хотите увидеть список возможных валют,введите currency");
        System.out.println("Если историю history и ISO валюты");
        System.out.println("Пример: history USD");
        System.out.println("Доступные тайм фреймы tomorrow, week");
        System.out.println("Для выхода введите exit");
    }

    /**
     * создает объект типа Request
     * @param inputString строка введенная пользователем
     * @return запрос
     */
    private static Request getRequestForPrediction(String inputString) {
        String[] param;
        Request request;
        try {
            param = inputString.split(" ");
            request = new Request(param);
            if (!checkRequest(request)) {
                throw new IllegalArgumentException("Введите верный запрос");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            request = getRequestForPrediction();
        }
        return request;
    }

    /**
     * метод выводит исторические данные в консоль
     * @param inputString введенная строка
     */
    private static void getHistory(String inputString) {
        String[] ISO = {"", inputString.split(" ")[1], ""};
        Request historyRequest = new Request(ISO);
        List<Case> data = null;
        try {
            data = DataHelper.getData(historyRequest);
            data.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Проверка запроса
     * Если введеный ISO код есть в базе доступных валют, то return true
     * Иначе выводится сообщение о том, что такая валюта недоступна или же
     * тайм фрейм введен неправильно
     * @param request запрос
     * @return результат проверки
     */

    private static boolean checkRequest(Request request) {
        boolean currency = Currency.getCurrencyMap().containsKey(request.getISO_Char_Code());
        boolean period = request.getTimeFrame().matches("week|tomorrow");//|month
        if (!currency) {
            System.out.println("невозможно получить информацию по этой валюте");
        }
        if (!period) {
            System.out.println("невозможно получить прогноз на данный период");
        }
        return currency && period;
    }
}
