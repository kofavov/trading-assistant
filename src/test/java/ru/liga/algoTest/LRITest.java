package ru.liga.algoTest;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.version2.algoritm.Algoritm;
import ru.liga.version2.algoritm.Average;
import ru.liga.version2.algoritm.LineRegressionFromInet;
import ru.liga.version2.model.Case;
import ru.liga.version2.model.Request;

import java.time.LocalDate;

public class LRITest {
    @Test
    public void weekLRITest() {
        Request request = new Request("/rate USD -period week -alg lri");
        Algoritm algoritm = new LineRegressionFromInet();
        LocalDate dateAfterWeek = LocalDate.now().plusWeeks(1);
        Case tomorrowCase = algoritm.getPrediction(request).get("USD").get(dateAfterWeek);
        Assert.assertTrue(tomorrowCase.getDate().isEqual(dateAfterWeek)
                && algoritm.getPrediction(request).get("USD").size() == 5);
    }

    @Test
    public void monthLRITest(){
        Request request = new Request("/rate USD -period month -alg lri");
        Algoritm algoritm = new LineRegressionFromInet();
        int sizePredictionMap = algoritm.getPrediction(request).get("USD").size();
        Assert.assertTrue(sizePredictionMap > 20 && sizePredictionMap < 32);
    }
    @Test
    public void tomorrowLRITest() {
        Request request = new Request("/rate USD -period tomorrow -alg lri");
        Algoritm algoritm = new LineRegressionFromInet();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Case tomorrowCase = algoritm.getPrediction(request).get("USD").get(tomorrow);
        Assert.assertTrue(tomorrowCase.getDate().isEqual(tomorrow)
                && algoritm.getPrediction(request).get("USD").size() == 1);
    }
}
