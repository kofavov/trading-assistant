package ru.liga.algoTest;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.version2.algoritm.Actual;
import ru.liga.version2.algoritm.Algoritm;
import ru.liga.version2.algoritm.Average;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.Request;

import java.time.LocalDate;

public class ActTest {
    @Test
    public void weekActTest() {
        Request request = new Request("/rate USD -period week -alg act");
        Algoritm algoritm = new Actual();
        LocalDate dateAfterWeek = LocalDate.now().plusWeeks(1);
        Case tomorrowCase = algoritm.getPrediction(request).get("USD").get(dateAfterWeek);
        Assert.assertTrue(algoritm.getPrediction(request).get("USD").size() == 5);
    }

    @Test
    public void monthActTest(){
        Request request = new Request("/rate USD -period month -alg act");
        Algoritm algoritm = new Actual();
        int sizePredictionMap = algoritm.getPrediction(request).get("USD").size();
        Assert.assertTrue(sizePredictionMap > 15 && sizePredictionMap < 32);
    }
    @Test
    public void tomorrowActTest() {
        Request request = new Request("/rate USD -period tomorrow -alg act");
        Algoritm algoritm = new Actual();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Case tomorrowCase = algoritm.getPrediction(request).get("USD").get(tomorrow);
        Assert.assertEquals(1, algoritm.getPrediction(request).get("USD").size());
    }
}
