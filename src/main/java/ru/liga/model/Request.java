package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Request {
    private String typeRequest;
    private String ISO_Char_Code;//USD, EUR, TRY и т.д.
    private String period;//tomorrow, week
    private boolean exit = false;
    private String algoritm;
    private String output;


    public Request(String commandString) {
        String[] simpleCommands = commandString.split(" ");

        for (int i = 0; i < simpleCommands.length; i++) {
            if (i == 0) this.typeRequest = simpleCommands[0];
            if (i == 1) this.ISO_Char_Code = simpleCommands[1];
            if (simpleCommands[i].equals("-period")) {
                this.period = simpleCommands[++i];
            } else if (simpleCommands[i].equals("-alg")) {
                this.algoritm = simpleCommands[++i];
            } else if (simpleCommands[i].equals("-output")) {
                this.output = simpleCommands[++i];
            }
        }
    }

    public Request(String typeRequest, String ISO_Char_Code, String timeFrame) {
        this.typeRequest = typeRequest;
        this.ISO_Char_Code = ISO_Char_Code;
        this.period = timeFrame;
    }

    public Request(boolean exit) {
        this.exit = exit;
    }

    public void setAlgoritm(String algoritm) throws Exception {
        if (!algoritm.equals("avg") && !algoritm.equals("lr")) {
            throw new Exception("Введите алгоритм из списка");
        }
        this.algoritm = algoritm;
    }

    @Override
    public String toString() {
        return typeRequest + " " + ISO_Char_Code + " " + period;
    }
}
