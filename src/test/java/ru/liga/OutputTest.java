package ru.liga;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

public class OutputTest {
    //работает только если подключение нормальное
    //нужен общий сканер в RequestHelper
    @Test
    public void checkUSDOutputWithConnectCBRF() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD week\r\navg\r\nexit".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException ignored) {
        }
        String[] outputStrings = output.toString().split("\r\n");
        boolean checkOneString = false;
        for (int i = 11; i < outputStrings.length-2; i++) {
            boolean date = outputStrings[i].matches("[а-я]{2} \\d{2}\\.\\d{2}\\.\\d{4} - .+");
            boolean price = outputStrings[i].matches(".+ \\d{2},\\d{2}");
            checkOneString = date && price;
            if(!checkOneString)break;
        }

        Assert.assertTrue(checkOneString);
    }
}
