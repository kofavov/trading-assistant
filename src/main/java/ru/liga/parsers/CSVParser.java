package ru.liga.parsers;

import au.com.bytecode.opencsv.CSVReader;
import ru.liga.model.Case;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser{
    @SuppressWarnings("resource")
    public static List<Case> getData(String[] request) {
        String currency = request[1];
        List<String[]> allRows = null;
        List<Case> allData = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(currency + ".csv"), ';', '"', 1);
            allRows = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Read CSV line by line and use the string array as you want
        for (String[] row : allRows) {
            allData.add(new Case(row));
        }
        return allData;
    }
}
