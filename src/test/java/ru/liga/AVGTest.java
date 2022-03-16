//package ru.liga;
//
//import org.junit.Assert;
//import org.junit.Test;
//import ru.liga.algoritms.Algo;
//import ru.liga.algoritms.AverageForTheWeekAlgo;
//import ru.liga.model.Case;
//import ru.liga.model.Request;
//import ru.liga.parsers.CSVParser;
//import ru.liga.parsers.Parser;
//
//import java.io.*;
//import java.util.List;
//
//public class AVGTest {
//
//    @Test
//    public void weekAvgTest() {
//        Request request = new Request("rate", "USD", "week");
//        CSVParser csvParser = new CSVParser();
//        List<Case> data = csvParser.getOldData(request);
//        Algo algo = new AverageForTheWeekAlgo(data, request);
//        List<Case> futurePoint = algo.getPrediction();
//        Assert.assertEquals(7, futurePoint.size());
//    }
//
//    @Test
//    public void tomorrowAvgTest() {
//        Request request = new Request("rate", "USD", "tomorrow");
//        CSVParser csvParser = new CSVParser();
//        List<Case> data = csvParser.getOldData(request);
//        Algo algo = new AverageForTheWeekAlgo(data, request);
//        List<Case> futurePoint = algo.getPrediction();
//        Assert.assertEquals(1, futurePoint.size());
//    }
//
//    @Test
//    public void checkOrderWeekAvg() {
//        Request request = new Request("rate", "USD", "week");
//        CSVParser csvParser = new CSVParser();
//        List<Case> data = csvParser.getOldData(request);
//        Algo algo = new AverageForTheWeekAlgo(data, request);
//        List<Case> futurePoint = algo.getPrediction();
//        boolean check = false;
//        for (int i = 0; i < futurePoint.size() - 2; i++) {
//            check = futurePoint.get(i).getDate().isBefore(futurePoint.get(++i).getDate());
//        }
//        Assert.assertTrue(check);
//    }
//
//
//}
