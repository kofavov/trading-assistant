package ru.liga.helpers;

import ru.liga.model.Currency;
import ru.liga.model.Request;

import java.util.Scanner;

public class RequestHelper {
    public static Request getRequest() {
        System.out.println("Введите запрос");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        String [] param = new String[0];
        Request request = null;
        try {
            param = s.split(" ");
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

    public static boolean checkRequest(Request request) {
        boolean currency = Currency.getCurrencyHashMap().containsKey(request.getISO_Char_Code());
        boolean period = request.getTimeFrame().contains("week") || request.getTimeFrame().contains("tomorrow");
        return currency && period;
    }
}
