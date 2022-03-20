package ru.liga;

import org.junit.Assert;
import org.junit.Test;
import ru.liga.version2.algoritm.Average;
import ru.liga.version2.exception.RequestException;
import ru.liga.version2.helper.RequestHelper;
import ru.liga.version2.model.Request;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;


public class RequestTest {
    @Test
    public void checkBadRequest() {
        Request request = new Request("/Bad request");
        RequestHelper requestHelper = new RequestHelper(request);

        Assert.assertEquals(requestHelper.executeTextRequest(),"неверный тип запроса");
    }

    @Test
    public void checkBadRequestWithoutTruePeriod() {
        String command = "/rate USD -period noSuchPeriod";
        String expectedMessage = "Нет такого периода";
        Assert.assertThrows(expectedMessage,RequestException.class,()->new Request(command));
    }

    @Test
    public void checkBadWithoutTrueAlgoritm() {
        String command = "/rate USD -period week -alg noSucAlg";
        String expectedMessage = "алгоритм не найден";
        Assert.assertThrows(expectedMessage,RequestException.class,()->new Request(command));
    }

    @Test
    public void checkNormalRequest() throws IOException {
        String command = "/rate USD -period week -alg avg";
        Request request = new Request(command);
        boolean normalRequest = request.getTypeRequest().equals("/rate")
                && request.getISO_Char_Codes().get(0).equals("USD")
                && request.getAlgoritm().getClass().equals(Average.class)
                && request.getPeriod().equals("week")
                && request.getDate().isEqual(LocalDate.now())
                && request.getStopDay().isEqual(LocalDate.now().plusWeeks(1));
        Assert.assertTrue(normalRequest);
    }
    @Test
    public void checkNormalTomorrowRequest() throws IOException {
        String command = "/rate USD -date tomorrow -alg avg";
        Request request = new Request(command);
        boolean normalRequest = request.getTypeRequest().equals("/rate")
                && request.getISO_Char_Codes().get(0).equals("USD")
                && request.getAlgoritm().getClass().equals(Average.class)
                && request.getPeriod().equals("tomorrow")
                && request.getDate().isEqual(LocalDate.now().plusDays(1))
                && request.getStopDay().isEqual(LocalDate.now().plusDays(1));
        Assert.assertTrue(normalRequest);
    }
}
