package ru.liga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.liga.helpers.RequestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
//по отдельности тесты проходятся вместе нет
public class RequestTest {

    @Test
    public void checkBadRequest() {
        ByteArrayInputStream in = new ByteArrayInputStream("Bad request\r\n".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequestForPrediction();
        } catch (NoSuchElementException ignored) {
        }
        boolean checkOutput = isCheckOutput(output);
        Assert.assertTrue(checkOutput);
    }

    @Test
    public void checkBadRequest2() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD noSuchPeriod\r\n".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        try {
            RequestHelper.getRequestForPrediction();
        } catch (NoSuchElementException ignored) {
        }
        boolean checkOutput = isCheckOutput(output);
        Assert.assertTrue(checkOutput);

    }

    @Test
    public void checkBadRequest3() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate noSuchCurrency week\r\n".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequestForPrediction();
        } catch (NoSuchElementException ignored) {
        }
        boolean checkOutput = isCheckOutput(output);

        Assert.assertTrue(checkOutput);

    }

    @Test
    public void checkNormalRequest() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD week\r\n".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequestForPrediction();
        } catch (NoSuchElementException ignored) {
        }
        boolean checkOutput = isCheckOutput(output);
        in.close();
        System.setIn(System.in);
        Assert.assertFalse(checkOutput);

    }

    private boolean isCheckOutput(ByteArrayOutputStream output) {
        String[] splitStrings = output.toString().split("\r(\n?)");
        boolean checkOutput = false;
        for (String s : splitStrings) {
            checkOutput = s.contains("Введите верный запрос");
            if (checkOutput) break;
        }
        return checkOutput;
    }
}
