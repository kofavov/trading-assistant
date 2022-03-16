package ru.liga.version2.model;

import ru.liga.version2.parser.CBRFExchange;
import ru.liga.version2.parser.CSVParser;

import java.time.LocalDate;
import java.util.*;

public class DataTable {
    private static final HashMap<String, TreeMap<LocalDate,Case>> DATA = new HashMap<>();
    static {
        CSVParser csvParser = new CSVParser();
        HashMap<String,TreeMap<LocalDate,Case>> csvData = csvParser.getData();
        HashMap<String,TreeMap<LocalDate,Case>> cbrfData = new CBRFExchange().getData();
        DATA.putAll(cbrfData);
        for (Map.Entry<String,TreeMap<LocalDate,Case>> cbData:cbrfData.entrySet()) {
            String iso = cbData.getKey();
            if (csvData.containsKey(iso)){
                merge(csvData.get(iso),cbData.getValue(),iso);
            }
            csvParser.save(iso,DATA.get(iso));
        }
    }

    private static void merge(TreeMap<LocalDate, Case> csvData, TreeMap<LocalDate, Case> cbData,String iso) {
        csvData.putAll(cbData);
        DATA.put(iso,csvData);
    }

    public static boolean init(){return !DATA.isEmpty();}


    public static HashMap<String, TreeMap<LocalDate, Case>> getDATA() {
        return DATA;
    }

    public static String getCurrenciesToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Доступные валюты").append("\n");
        for (Map.Entry<String, TreeMap<LocalDate,Case>> currency:DATA.entrySet()) {
            Case oneCaseData = currency.getValue().firstEntry().getValue();
            stringBuilder.append(oneCaseData.getISOCharCode()).append(" ")
                    .append(oneCaseData.getName()).append(" ")
                    .append("nominal=").append(oneCaseData.getNominal()).append("\n");
        }
        return stringBuilder.toString();
    }

    public static String getHistoryToString(Request request){
        StringBuilder stringBuilder = new StringBuilder();
        for (String iso :request.getISO_Char_Codes()) {
            List<Case> cases = new ArrayList<>(DATA.get(iso).values());
            Collections.reverse(cases);
            cases = cases.subList(0,30);
            stringBuilder.append("history ").append(iso).append(" ")
                    .append(cases.get(0).getName()).append("\n");
            for (Case c:cases) {
                stringBuilder.append(c).append("\n");
            }
        }
        return stringBuilder.toString();
    }

}
