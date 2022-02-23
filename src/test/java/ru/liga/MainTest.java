package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.algoritms.Algo;
import ru.liga.algoritms.AverageSevenDays;
import ru.liga.model.Case;
import ru.liga.parsers.CSVParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class MainTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private StringBuilder stringBuilder;

    @Test
    public void weekAvgTest() throws IOException {
        String[] request = new String[]{"rate", "USD", "week"};
        Algo algo = new AverageSevenDays();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        Assert.assertEquals(7, futurePoint.size());
    }

    @Test
    public void tomorrowAvgTest() {
        String[] request = new String[]{"rate", "USD", "tomorrow"};
        Algo algo = new AverageSevenDays();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        Assert.assertEquals(1, futurePoint.size());
    }

    @Test
    public void checkOrderWeekAvg() {
        String[] request = new String[]{"rate", "USD", "week"};
        Algo algo = new AverageSevenDays();
        List<Case> data = CSVParser.getData(request);
        List<Case> futurePoint = algo.getPrediction(data, request);
        boolean check = false;
        for (int i = 0; i < futurePoint.size() - 2; i++) {
            check = futurePoint.get(i).getDate().isBefore(futurePoint.get(++i).getDate());
        }
        Assert.assertTrue(check);
    }


}
