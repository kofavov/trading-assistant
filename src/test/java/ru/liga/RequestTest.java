package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.helpers.RequestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

public class RequestTest {
    @Test
    public void checkBadRequest(){
        ByteArrayInputStream in = new ByteArrayInputStream("Bad request".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequest();
        }catch (NoSuchElementException ignored){
        }
        Assert.assertTrue(output.toString().contains("Введите верный запрос"));
    }
    @Test
    public void checkBadRequest2() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD noSuchPeriod".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequest();
        } catch (NoSuchElementException ignored) {
        }
        Assert.assertTrue(output.toString().contains("Введите верный запрос"));
    }
    @Test
    public void checkBadRequest3() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate noSuchCurrency week".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequest();
        } catch (NoSuchElementException ignored) {
        }
        Assert.assertTrue(output.toString().contains("Введите верный запрос"));
    }
    @Test
    public void checkNormalRequest() {
        ByteArrayInputStream in = new ByteArrayInputStream("rate USD week".getBytes());
        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            RequestHelper.getRequest();
        } catch (NoSuchElementException ignored) {
        }
        Assert.assertFalse(output.toString().contains("Введите верный запрос"));
    }
}
