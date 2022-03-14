package ru.liga.parsers;

import au.com.bytecode.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
public class CSVParser implements ru.liga.parsers.Parser {
    private static final LinkedHashSet<LocalDate> FULL_MOON_SET = new LinkedHashSet<>();
    static {
        getFullMoon();
    }

    @SuppressWarnings("resource")
    public List<Case> getOldData(Request request) {
        List<Case> data = new ArrayList<>();
        File file = new File(request.getISO_Char_Code() + ".csv");
        if (file.exists()) {
            try {
                log.info("Получение данных из {}",file.getPath());
                FileReader fileReader = new FileReader(file);
                CSVReader reader = new CSVReader(fileReader, ';', '"', 1);

                for (int i = 0; i < 365; i++) {
                    String[] row = reader.readNext();
                    data.add(new Case(row));
                }

                reader.close();
            } catch (IOException e) {
                log.info("Не удалось прочитать файл {}",file);
                log.info(e.toString());
            }
        }
        return data;
    }


    public List<Case> getNewData(Request request) {
        List<Case> data = new ArrayList<>();
        File file = new File("tempRate\\new" + request.getISO_Char_Code() + ".csv");
        if (file.exists()) {
            try {
                log.info("Получение данных из {}",file.getPath());
                FileReader fileReader = new FileReader(file);
                CSVReader reader = new CSVReader(fileReader, ';', '"', 0);
                List<String[]> strings = new ArrayList<>(reader.readAll());
                for (String[] s : strings) {
                    data.add(new Case(s));
                }
                reader.close();
            } catch (IOException e) {
                log.info("Не удалось прочитать файл {}",file);
                log.info(e.toString());
            }
        }
        return data;
    }


    public void saveData(List<Case> newCases, Request request) {
        if (!newCases.isEmpty()) {
            File file = new File("tempRate\\new" + request.getISO_Char_Code() + ".csv");
            try {
                FileWriter fileWriter = new FileWriter(file, false);
                log.info("Сохранение файла {}",file.getPath());
                for (Case c : newCases) {

                    String addString = c.getNominal() + ";"
                            + c.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ";"
                            + '"' + c.getValue() + '"' + ";"
                            + c.getCurrency() + "\n";
                    fileWriter.write(addString);
                }
                fileWriter.close();
            } catch (IOException e) {
                log.info("Не удалось сохранить файл {}",file);
                log.info(e.toString());
            }
        }
    }


    public List<Case> getActAlgoData(Request request) {
        List<Case> data = new ArrayList<>();
        File file = new File(request.getISO_Char_Code() + ".csv");
        log.info("Получение данных из {}",file.getPath());
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(request.getISO_Char_Code() + ".csv");
                CSVReader reader = new CSVReader(fileReader, ';', '"', 1);
                List<String[]> strings = reader.readAll();
                LocalDate threeYearsAgo = LocalDate.now().minusYears(3);
                LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
                int timeFrame = DateHelper.getCountDays(request);
                if (timeFrame == 7) timeFrame += 2;
                if (timeFrame == 30) timeFrame += 8;
                for (String[] s : strings) {
                    Case c = new Case(s);
                    if (c.getDate().isAfter(twoYearsAgo) &&
                            c.getDate().isBefore(twoYearsAgo.plusDays(timeFrame).plusDays(1))) {
                        data.add(c);
                    }
                    if (c.getDate().isAfter(threeYearsAgo) &&
                            c.getDate().isBefore(threeYearsAgo.plusDays(timeFrame).plusDays(1))) {
                        data.add(c);
                    }
                }
                reader.close();
            } catch (IOException e) {
                log.info("Не удалось прочитать файл {} для актуального алгоритма",file);
                log.info(e.toString());
            }
        }else {
            log.info("Файла {} не существует",file);
        }
        return data;
    }

    public static LinkedHashSet<LocalDate> getFullMoonSet() {
        return FULL_MOON_SET;
    }

    private static void getFullMoon (){
        File file = new File("moon-phase-calendar-landscape.csv");
        if (file.exists()) {
            try {
                log.info("Получение данных из {}",file.getPath());
                FileReader fileReader = new FileReader(file);
                CSVReader reader = new CSVReader(fileReader, ',', '"', 1);
                List<String[]> strings = reader.readAll();
                for (String[] row : strings) {
                    if (row[0].equals("Full Moon")){
                    FULL_MOON_SET.add(LocalDate.parse(row[3],DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    }
                }
                reader.close();
            } catch (IOException e) {
                log.info("Не удалось прочитать файл {}",file);
                log.info(e.toString());
            }
        }
    }
}
