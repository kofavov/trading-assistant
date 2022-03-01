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
    private String timeFrame;//tomorrow, week
    private boolean exit = false;

    private String algoritm;


    public Request(String[] splitInputString) {
        if (splitInputString.length != 3) {
            throw new IllegalArgumentException("Введите верный запрос");
        }
        this.typeRequest = splitInputString[0];
        this.ISO_Char_Code = splitInputString[1];
        this.timeFrame = splitInputString[2];
    }

    public Request(String typeRequest, String ISO_Char_Code, String timeFrame) {
        this.typeRequest = typeRequest;
        this.ISO_Char_Code = ISO_Char_Code;
        this.timeFrame = timeFrame;
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
        return typeRequest + " " + ISO_Char_Code + " " + timeFrame;
    }
}
