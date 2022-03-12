package ru.liga.parsers;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.SneakyThrows;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements ru.liga.parsers.Parser {
    @SuppressWarnings("resource")
    public List<Case> getOldData(Request request) throws IOException {
        List<Case> data = new ArrayList<>();
        File file = new File(request.getISO_Char_Code() + ".csv");
        if (file.exists()) {
            FileReader fileReader = new FileReader(request.getISO_Char_Code() + ".csv");
            CSVReader reader = new CSVReader(fileReader, ';', '"', 1);

            for (int i = 0; i < 365; i++) {
                String[] row = reader.readNext();
                data.add(new Case(row));
            }

            reader.close();
        }
        return data;
    }
    @SneakyThrows
    public List<Case> getNewData(Request request)  {
        List<Case> data = new ArrayList<>();
        File file = new File("tempRate\\new" + request.getISO_Char_Code() + ".csv");
        if (file.exists()){
        FileReader fileReader = new FileReader("tempRate\\new" + request.getISO_Char_Code() + ".csv");

        CSVReader reader = new CSVReader(fileReader, ';', '"', 0);
        List<String[]> strings = new ArrayList<>(reader.readAll());
            for (String[] s: strings) {
                data.add(new Case(s));
            }
        reader.close();}
        return data;
    }

    @SneakyThrows
    public void saveData(List<Case> newCases, Request request) {
        if (!newCases.isEmpty()){
        FileWriter fileWriter = new FileWriter("tempRate\\new" + request.getISO_Char_Code() + ".csv",false);

        for (Case c : newCases) {

            String addString = c.getNominal() + ";"
                    + c.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ";"
                    + '"' + c.getValue() + '"' + ";"
                    + c.getCurrency()+"\n";
            fileWriter.write(addString);
        }
        fileWriter.close();
        }
    }

}
