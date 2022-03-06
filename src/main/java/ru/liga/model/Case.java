package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Объекты данного класса хранят в себе информацию о курсе на какую-то определенную дату
 */
@Getter
@Setter
@EqualsAndHashCode
public class Case {
    private LocalDate date;
    private double value;
    private String currency;
    private Integer nominal;
    //количество знаков после запятой
    private static final int SCALE = 2;

    public Case() {
    }

    public Case(String[] row) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.nominal = Integer.parseInt(row[0]);
        this.date = LocalDate.parse(row[1], dateTimeFormatter);
        String val = row[2].replaceAll(",", ".").replaceAll("\"","");
        this.value = Double.parseDouble(val);
        this.currency = row[3];
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy");
        String dateToString = date.format(dateTimeFormatter);
        double roundValue = Precision.round(value, SCALE);
        return dateToString + " - " + String.format("%." + SCALE + "f", roundValue);
    }
}
