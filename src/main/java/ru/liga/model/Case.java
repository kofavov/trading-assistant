package ru.liga.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Case {
    private LocalDate date;
    private double value;
    private String currency;

    public Case() {
    }

    public Case(String[] row) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.date = LocalDate.parse(row[0],dateTimeFormatter);
        this.value = Double.parseDouble(row[1].replaceAll(",","."));
        this.currency = row[2];
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case onePoint = (Case) o;
        return Double.compare(onePoint.value, value) == 0 && Objects.equals(date, onePoint.date) && Objects.equals(currency, onePoint.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, currency, value);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd-MM-yyyy");
        String dateToString = date.format(dateTimeFormatter);
        return dateToString + " " + String.format("%.2f", value) + " " + currency;
    }
}
