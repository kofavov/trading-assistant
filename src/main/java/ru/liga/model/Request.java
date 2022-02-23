package ru.liga.model;

public class Request {
    private String rate;
    private String ISO_Char_Code;
    private String timeFrame;

    public Request(String [] request) {
        if (request.length!=3){
            throw new IllegalArgumentException("Введите верный запрос");
        }
        this.rate = request[0];
        this.ISO_Char_Code = request[1];
        this.timeFrame = request[2];
    }

    public Request(String rate, String ISO_Char_Code, String timeFrame) {
        this.rate = rate;
        this.ISO_Char_Code = ISO_Char_Code;
        this.timeFrame = timeFrame;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
}
