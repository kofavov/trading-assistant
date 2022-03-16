package ru.liga.version2.parser;

import ru.liga.version2.model.Case;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

public interface Parser {
    HashMap<String, TreeMap<LocalDate, Case>> getData();
}
