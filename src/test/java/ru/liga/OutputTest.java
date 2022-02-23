package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.helpers.RequestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

public class OutputTest {
    @Test
    public void checkUSDOutput() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD week".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException ignored) {
        }
        String[] outputStrings = output.toString().split("\r\n");
        boolean checkOneString = false;
        for (int i = 1; i < outputStrings.length; i++) {
            boolean date = outputStrings[i].matches("[а-я]{2} \\d{2}\\.\\d{2}\\.\\d{4} - .+");
            boolean price = outputStrings[i].matches(".+ \\d{2},\\d{2} .+");
            boolean name = outputStrings[i].matches(".+ Доллар США");
            checkOneString = date && price && name;
            if(!checkOneString)break;
        }

        Assert.assertTrue(checkOneString);
    }
}
