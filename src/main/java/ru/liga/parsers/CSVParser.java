package ru.liga.parsers;

import au.com.bytecode.opencsv.CSVReader;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements ru.liga.parsers.Parser {
    @SuppressWarnings("resource")
    public List<Case> getData(Request request) throws IOException {
        List<String[]> allRows = null;
        List<Case> data = new ArrayList<>();

        FileReader fileReader = new FileReader(request.getISO_Char_Code() + ".csv");
        CSVReader reader = new CSVReader(fileReader, ';', '"', 1);
        allRows = reader.readAll();

        for (String[] row : allRows) {
            data.add(new Case(row));
        }
        return data;
    }
}
