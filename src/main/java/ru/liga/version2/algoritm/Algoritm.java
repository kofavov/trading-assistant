package ru.liga.version2.algoritm;

import ru.liga.version2.model.Case;
import ru.liga.version2.model.Request;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

public interface Algoritm {
    HashMap<String, TreeMap<LocalDate, Case>> getPrediction(Request request);
}
