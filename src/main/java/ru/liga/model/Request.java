package ru.liga.model;

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

    public String getAlgoritm() {
        return algoritm;
    }

    public void setAlgoritm(String algoritm) throws Exception {
        if (!algoritm.equals("avg") && !algoritm.equals("lr")) {
            throw new Exception("Введите алгоритм из списка");
        }
        this.algoritm = algoritm;
    }

    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public String getISO_Char_Code() {
        return ISO_Char_Code;
    }

    public void setISO_Char_Code(String ISO_Char_Code) {
        this.ISO_Char_Code = ISO_Char_Code;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public String toString() {
        return typeRequest + " " + ISO_Char_Code + " " + timeFrame;
    }
}
