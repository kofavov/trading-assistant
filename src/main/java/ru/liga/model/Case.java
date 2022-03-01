package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

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
    //количество знаков после запятой
    private static final int SCALE = 2;

    public Case() {
    }

    public Case(String[] row) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.date = LocalDate.parse(row[0], dateTimeFormatter);
        this.value = Double.parseDouble(row[1].replaceAll(",", "."));
        this.currency = row[2];
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy");
        String dateToString = date.format(dateTimeFormatter);
        double roundValue = Precision.round(value, SCALE);
        return dateToString + " - " + String.format("%." + SCALE + "f", roundValue);
    }
}
