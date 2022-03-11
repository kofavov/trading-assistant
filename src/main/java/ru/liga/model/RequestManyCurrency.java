package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class RequestManyCurrency {
    private String typeRequest = "";
    private List<String> ISO_Char_Codes = new ArrayList<>();//USD, EUR, TRY и т.д.
    private String period = "";//tomorrow, week
    private String algoritm = "";
    private String output = "";
    private LocalDate date = LocalDate.now();

    public RequestManyCurrency(String commandString) {
        String[] simpleCommands = commandString.split(" ");

        for (int i = 0; i < simpleCommands.length; i++) {
            if (i == 0) this.typeRequest = simpleCommands[0];
            if (i == 1) this.ISO_Char_Codes = Arrays.asList(simpleCommands[1].split(","));
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

    @Override
    public String toString() {
        return
                typeRequest +
                        ", " + ISO_Char_Codes +
                        ", " + period +
                        ", " + algoritm +
                        ", " + output +
                        ", " + date;
    }
}
