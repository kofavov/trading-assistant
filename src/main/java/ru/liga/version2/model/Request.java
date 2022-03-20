package ru.liga.version2.model;

import lombok.Data;
import ru.liga.version2.algoritm.Algoritm;
import ru.liga.version2.algoritm.AlgoritmFactory;
import ru.liga.version2.exception.RequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Data
public class Request {
    private String typeRequest = "";
    private List<String> ISO_Char_Codes = List.of("");//USD, EUR, TRY и т.д.
    private LocalDate stopDay = LocalDate.now();
    private String period = "";//tomorrow, week
    private Algoritm algoritm = null;
    private String output = "";
    private LocalDate date = LocalDate.now();

    public Request(String commandString) throws RequestException {
        String[] simpleCommands = commandString.split(" ");
        for (int i = 0; i < simpleCommands.length; i++) {
            if (i == 0) this.typeRequest = simpleCommands[0];
            if (i == 1) this.ISO_Char_Codes = Arrays.asList(simpleCommands[1].split(","));
            switch (simpleCommands[i]) {
                case "-period":
                    this.period = simpleCommands[++i];
                    break;
                case "-alg":
                    this.algoritm = new AlgoritmFactory().getAlgoritm(simpleCommands[++i]);
                    break;
                case "-output":
                    this.output = simpleCommands[++i];
                    break;
                case "-date":
                    calculateStartDate(simpleCommands[++i]);
                    break;
            }
        }
        calculateStopDate();
    }

    private void calculateStartDate(String s) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (s.equals("tomorrow")) {
            this.period = s;
            date = LocalDate.now().plusDays(1);
        } else {
            this.date = LocalDate.parse(s, dateTimeFormatter);
            stopDay = date;
        }
    }

    private void calculateStopDate(){
        switch (period){
            case "tomorrow": stopDay = stopDay.plusDays(1);break;
            case "week": stopDay = stopDay.plusWeeks(1);break;
            case "month": stopDay = stopDay.plusMonths(1);break;
            case "" : break;
            default:throw new RequestException("Нет такого периода");
        }
    }

    public LocalDate getStopDay() {
        return stopDay;
    }

    @Override
    public String toString() {
        return typeRequest +
                " " + ISO_Char_Codes +
                " " + period +
                " " + algoritm +
                " " + output +
                " " + date;
    }
}
