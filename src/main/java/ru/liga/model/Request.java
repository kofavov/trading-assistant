package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
@EqualsAndHashCode
public class Request {
    private String typeRequest = "";
    private String ISO_Char_Code = "";//USD, EUR, TRY и т.д.
    private String period = "";//tomorrow, week
    private String algoritm = "";
    private String output = "";
    private LocalDate date = LocalDate.now();


    public Request(String commandString) {
        String[] simpleCommands = commandString.split(" ");

        for (int i = 0; i < simpleCommands.length; i++) {
            if (i == 0) this.typeRequest = simpleCommands[0];
            if (i == 1) this.ISO_Char_Code = simpleCommands[1].toUpperCase(Locale.ROOT);
            switch (simpleCommands[i]) {
                case "-period":
                    this.period = simpleCommands[++i];
                    break;
                case "-alg":
                    this.algoritm = simpleCommands[++i];
                    break;
                case "-output":
                    this.output = simpleCommands[++i];
                    break;
                case "-date":
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    this.date = LocalDate.parse(simpleCommands[++i], dateTimeFormatter);
                    break;
            }
        }
    }

    public Request(String typeRequest, String ISO_Char_Code, String timeFrame) {
        this.typeRequest = typeRequest;
        this.ISO_Char_Code = ISO_Char_Code.toUpperCase(Locale.ROOT);
        this.period = timeFrame;
    }

    public Request(String typeRequest, String ISO_Char_Code, String timeFrame,String algoritm,String output,LocalDate date) {
        this.typeRequest = typeRequest;
        this.ISO_Char_Code = ISO_Char_Code.toUpperCase(Locale.ROOT);
        this.period = timeFrame;
        this.algoritm = algoritm;
        this.output = output;
        this.date = date;
    }

    public Request(RequestManyCurrency requestManyCurrency, String s) {
        this.ISO_Char_Code = s.toUpperCase(Locale.ROOT);
        this.period = requestManyCurrency.getPeriod();
        this.date = requestManyCurrency.getDate();
        this.typeRequest = requestManyCurrency.getTypeRequest();
        this.output = requestManyCurrency.getOutput();
        this.algoritm = requestManyCurrency.getAlgoritm();
    }

    @Override
    public String toString() {
        return typeRequest + " " + ISO_Char_Code + " " + period + " " + algoritm;
    }
}
