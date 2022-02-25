package ru.liga.helpers;

import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RequestHelper {
    public static Request getRequest() {
        Request request = null;
        String[] param;

        helpText();
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();

        if (inputString.equals("currency")) {
            Currency.getCurrencyMap().values().forEach(System.out::println);
            request = getRequest();
        } else if (inputString.contains("history")) {
            getHistory(inputString);
            request = getRequest();
        } else if (inputString.equals("exit")) {
            System.exit(0);
        } else {
            request = getRequest(inputString);
        }
        return request;
    }

    private static void helpText() {
        System.out.println("Введите запрос");
        System.out.println("Пример: rate USD week");
        System.out.println("Если хотите увидеть список возможных валют,введите currency");
        System.out.println("Если историю history и ISO валюты");
        System.out.println("Пример: history USD");
        System.out.println("Доступные тайм фреймы tomorrow, week, month");
        System.out.println("Для выхода введите exit");
    }

    private static Request getRequest(String inputString) {
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
            request = getRequest();
        }
        return request;
    }

    private static void getHistory(String s) {
        String[] ISO = {"rate", s.split(" ")[1], "tomorrow"};
        Request historyRequest = new Request(ISO);
        List<Case> data = null;
        try {
            data = DataHelper.getData(historyRequest);
            data.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkRequest(Request request) {
        boolean currency = Currency.getCurrencyMap().containsKey(request.getISO_Char_Code());
        boolean period = request.getTimeFrame().matches("week|tomorrow|month");
        if (!currency) {
            System.out.println("невозможно получить информацию по этой валюте");
        }
        if (!period) {
            System.out.println("невозможно получить прогноз на данный период");
        }
        return currency && period;
    }
}
