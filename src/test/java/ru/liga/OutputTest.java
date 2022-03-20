package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.version2.helper.RequestHelper;
import ru.liga.version2.model.Request;

import java.io.File;


public class OutputTest {

    @Test
    public void checkUSDHistoryRequest() {
        Request request = new Request("/history USD");
        RequestHelper requestHelper = new RequestHelper(request);

        String[] outputStrings = requestHelper.executeTextRequest().split("\n");
        boolean firstString = outputStrings[0].equals("history USD Доллар США");

        boolean checkStrings = checkStringWithValue( outputStrings);
        Assert.assertTrue(checkStrings && firstString);
    }

    @Test
    public void checkUSDRateRequest() {
        Request request = new Request("/rate USD -period week -alg avg");
        RequestHelper requestHelper = new RequestHelper(request);

        String[] outputStrings = requestHelper.executeTextRequest().split("\n");
        boolean firstString = outputStrings[0].equals("Predict for USD");

        boolean checkStrings = checkStringWithValue( outputStrings);
        Assert.assertTrue(checkStrings && firstString);
    }
    @Test
    public void graphOutputTest() throws Exception {
        File file = new File("graph.png");
        boolean delete = file.delete();

        Request request = new Request("/rate USD -period week -alg avg");
        RequestHelper requestHelper = new RequestHelper(request);

        requestHelper.executeGraphRequest();

        Assert.assertTrue(file.exists() && delete);
    }

    private boolean checkStringWithValue(String[] outputStrings) {
        boolean checkOneString = false;
        for (int i = 1; i < outputStrings.length; i++) {
            boolean date = outputStrings[i].matches("[а-я]{2} \\d{2}\\.\\d{2}\\.\\d{4} - .+");
            boolean price = outputStrings[i].matches(".+ \\d{2,3},\\d{2}");
            checkOneString = date && price;
            if (!checkOneString) break;
        }
        return checkOneString;
    }
}
