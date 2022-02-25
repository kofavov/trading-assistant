package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageForTheWeekAlgo;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;
import ru.liga.parsers.CSVParser;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;

public class AVGTest {


    @Test
    public void weekAvgTest() throws IOException {
        Request request = new Request("rate", "USD", "week");
        Algo algo = new AverageForTheWeekAlgo();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        Assert.assertEquals(7, futurePoint.size());
    }

    @Test
    public void tomorrowAvgTest() throws IOException {
        Request request = new Request("rate", "USD", "tomorrow");
        Algo algo = new AverageForTheWeekAlgo();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        Assert.assertEquals(1, futurePoint.size());
    }

    @Test
    public void checkOrderWeekAvg() throws IOException {
        Request request = new Request("rate", "USD", "week");
        Algo algo = new AverageForTheWeekAlgo();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        boolean check = false;
        for (int i = 0; i < futurePoint.size() - 2; i++) {
            check = futurePoint.get(i).getDate().isBefore(futurePoint.get(++i).getDate());
        }
        Assert.assertTrue(check);
    }


}
