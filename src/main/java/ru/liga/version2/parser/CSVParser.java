package ru.liga.version2.parser;

import au.com.bytecode.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.model.Case;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CSVParser implements Parser {
    public HashMap<String, TreeMap<LocalDate, Case>> getData() {
        HashMap<String, TreeMap<LocalDate, Case>> data = new HashMap<>();
        List<Path> paths;
        try {
            paths = Files.walk(Path.of("oldData")).collect(Collectors.toList());
            for (Path p : paths) {
                if (p.getFileName().toString().matches("...\\.csv")) {
                    TreeMap<LocalDate, Case> oldData = getOldData(p);
                    data.put(p.toFile().getName().substring(0,3), oldData);
                }
            }
        } catch (IOException e) {
            log.info("Не удалось прочитать файлы");
        }
        return data;
    }

    private static TreeMap<LocalDate, Case> getOldData(Path path) {
        TreeMap<LocalDate, Case> data = new TreeMap<>();
        try {
            log.info("Получение данных из {}", path);
            FileReader fileReader = new FileReader(path.toFile());
            CSVReader reader = new CSVReader(fileReader, ';', '"', 1);
            List<String[]> strings = reader.readAll();
            for (int i = 0; i < (Math.min(strings.size(), 1100)); i++) {
                String iso = path.getFileName().toString().substring(0,3);
                Case c = new Case(strings.get(i), iso);
                data.put(c.getDate(), c);
            }
            reader.close();
        } catch (IOException e) {
            log.info("Не удалось прочитать файл {}", path);
            log.info(e.toString());
        }
        return data;
    }


    public List<LocalDate> getFullMoon() {
        File file = new File("moon-phase-calendar-landscape.csv");
        ArrayList<LocalDate> fullMoons = new ArrayList<>();
        if (file.exists()) {
            try {
                log.info("Получение данных из {}", file.getPath());
                FileReader fileReader = new FileReader(file);
                CSVReader reader = new CSVReader(fileReader, ',', '"', 1);
                List<String[]> strings = reader.readAll();
                for (String[] row : strings) {
                    if (row[0].equals("Full Moon")) {
                        fullMoons.add(LocalDate.parse(row[3], DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    }
                }
                reader.close();
            } catch (IOException e) {
                log.info("Не удалось прочитать файл {}", file);
                log.info(e.toString());
            }
        }
        return fullMoons;
    }

    public void save(String iso, TreeMap<LocalDate, Case> saveData) {
        File file = new File("oldData\\" + iso + ".csv");
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            log.info("Сохранение файла {}", file.getPath());
            fileWriter.write("nominal;data;curs;cdx\n");
            List<Case> saveCases = new ArrayList<>(saveData.values());
            Collections.reverse(saveCases);
            for (Case c : saveCases) {
                String addString = c.getNominal() + ";"
                        + c.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ";"
                        + '"' + c.getValue() + '"' + ";"
                        + c.getName() + "\n";
                fileWriter.write(addString);
            }
            fileWriter.close();
        } catch (IOException e) {
            log.info("Не удалось сохранить файл {}", file);
            log.info(e.toString());
        }
    }
}
