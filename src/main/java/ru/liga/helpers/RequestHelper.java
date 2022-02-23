package ru.liga.helpers;

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
        boolean currency = request[1].contains("USD") || request[1].contains("TRY") || request[1].contains("EUR");
        boolean period = request[2].contains("week") || request[2].contains("tomorrow");
        return currency && period;
    }
}
