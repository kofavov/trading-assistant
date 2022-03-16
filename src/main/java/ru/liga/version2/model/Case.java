package ru.liga.version2.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Объекты данного класса хранят в себе информацию о курсе на какую-то определенную дату
 */
@Data
@NoArgsConstructor
public class Case {
    private LocalDate date;
    private double value;
    private String ISOCharCode;
    private Integer nominal;
    private String name;
    //количество знаков после запятой
    private static final int SCALE = 2;

    public Case(String[] row, String ISOCharCode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.nominal = Integer.parseInt(row[0]);
        this.date = LocalDate.parse(row[1], dateTimeFormatter);
        String val = row[2].replaceAll(",", ".").replaceAll("\"", "");
        this.value = Double.parseDouble(val);
        this.ISOCharCode = ISOCharCode;
        this.name = row[3];
    }

    public Case(Node node, LocalDate date) {
        NodeList nodeList = node.getChildNodes();
        this.date = date;
        Node nodeVal = nodeList.item(4);
        this.value = Double.parseDouble(nodeVal.getFirstChild().getNodeValue().replaceAll(",", "."));
        Node nodeCur = nodeList.item(1);
        this.ISOCharCode = nodeCur.getFirstChild().getNodeValue();
        Node nodeNom = nodeList.item(2);
        this.nominal = Integer.parseInt(nodeNom.getFirstChild().getNodeValue());
        Node nodeName = nodeList.item(3);
        this.name = nodeName.getFirstChild().getNodeValue();
    }

    public Case(Case oldCase, double value, LocalDate tempDate) {
        this.name = oldCase.getName();
        this.ISOCharCode = oldCase.getISOCharCode();
        this.nominal = oldCase.getNominal();
        this.date = tempDate;
        this.value = value;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy");
        String dateToString = date.format(dateTimeFormatter);
        double roundValue = Precision.round(value, SCALE);
        return dateToString + " - " + String.format("%." + SCALE + "f", roundValue);
    }
}