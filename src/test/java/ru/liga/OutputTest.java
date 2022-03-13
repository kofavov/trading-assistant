package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.RequestManyCurrency;

import java.util.Arrays;

public class OutputTest {
    //работает только если подключение нормальное
    //нужен общий сканер в RequestHelper
    @Test
    public void checkUSDHistoryRequest() {
        RequestManyCurrency request = new RequestManyCurrency("/history USD");
        RequestHelper requestHelper = new RequestHelper(request);
        boolean checkOneString = false;
        String[] outputStrings = requestHelper.executeRequest().split("\n");
        boolean firstString = outputStrings[0].equals("history for USD");
//        System.out.println(Arrays.toString(outputStrings));
        for (int i = 1; i < outputStrings.length-2; i++) {
            boolean date = outputStrings[i].matches("[а-я]{2} \\d{2}\\.\\d{2}\\.\\d{4} - .+");
            boolean price = outputStrings[i].matches(".+ \\d{2,3},\\d{2}");
            checkOneString = date && price;
            if(!checkOneString)break;
        }
        Assert.assertTrue(checkOneString&&firstString);
    }
}
