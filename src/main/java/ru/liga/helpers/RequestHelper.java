package ru.liga.helpers;

import ru.liga.model.Currency;

import java.util.Scanner;

public class RequestHelper {
    public static String[] getRequestParam() {
        System.out.println("Введите запрос");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        String [] param = s.split(" ");
        if (!checkRequest(param)) {
            System.out.println("Введите верный запрос");
            param = getRequestParam();
        }
        return param;
    }

    public static boolean checkRequest(String[] request) {
        boolean currency = Currency.getCurrencyHashMap().containsKey(request[1]);
        boolean period = request[2].contains("week") || request[2].contains("tomorrow");
        return currency && period;
    }
}
