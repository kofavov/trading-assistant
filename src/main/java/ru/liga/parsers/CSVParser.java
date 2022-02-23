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
        List<Case> data = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(currency + ".csv");
            CSVReader reader = new CSVReader(fileReader, ';', '"', 1);
            allRows = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String[] row : allRows) {
            data.add(new Case(row));
        }
        return data;
    }
}
