package ru.liga.parsers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.liga.helpers.DateHelper;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CBRFExchange implements Parser {

    public List<Case> getData(Request request,int missDay) throws Exception {
        Currency currency = Currency.getCurrencyMap().get(request.getISO_Char_Code());
        List<Case> data = new ArrayList<>();
//        if (request.getAlgoritm().equals("act")){
//             getDataForActualAlgo(data,request,currency);
//             return data;
//        }
        //возможно есть инфа на завтра
        LocalDate tomorrow = request.getDate().plusDays(1);

        log.info("Выполнение запроса {} к ЦБ ",request);
        getDataFromCBRF(data, tomorrow, missDay, currency);
        Collections.reverse(data);
        return data;
    }
    @SneakyThrows
    public void getDataForActualAlgo(List<Case> data,Request request, Currency currency){
        List<List<Case>> doubleData = new ArrayList<>();
        while ( doubleData.size() < 2) {
            List<Case> tempList = new ArrayList<>();
            LocalDate tempDay = request.getDate().minusYears(doubleData.size()+2);
            tempDay = DateHelper.checkDayOfWeek(tempDay);
            int historyTimeFrame = DateHelper.getCountDays(request);
            if (historyTimeFrame == 7) tempDay = tempDay.plusDays(2);
            getDataFromCBRF(tempList, tempDay.plusDays(historyTimeFrame), historyTimeFrame,currency);
            doubleData.add(tempList);
        }
        data.addAll(doubleData.get(0));
        data.addAll(doubleData.get(1));
    }



    private void getDataFromCBRF(List<Case> data, LocalDate tomorrow, int missDays, Currency currency) throws Exception {
        for (int i = 0; i <= missDays; i++) {

            //сервер допускает 5 запросов в секунду
            //sleep вначале так как выполняется запрос getCurrencyMap
            try {
                Thread.sleep(210);
            } catch (InterruptedException ignored) {

            }
            LocalDate tempDay = tomorrow.minusDays(i);
            if (DateHelper.newCheckDayOfWeek(tempDay)) {
                continue;
            }
            getDataFromXml(data, currency, tempDay);
        }
    }

    private void getDataFromXml(List<Case> data, Currency currency, LocalDate tempDay) throws IOException, ParserConfigurationException {
        URLConnection connection = getUrlConnection(tempDay);
        connection.setConnectTimeout(1500);
        connection.setReadTimeout(1500);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;
        try {
            //Возникает Exception если на данный день нет данных(например выходной или праздник)
            doc = builder.parse(connection.getInputStream());
        } catch (Exception e) {
            return;
        }
        NodeList curElements = doc.getDocumentElement().getElementsByTagName("Valute");
        getCases(currency, data, curElements, tempDay);
    }

    private void getCases(Currency currency, List<Case> caseList, NodeList curElements, LocalDate date) {
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            String id = attributes.getNamedItem("ID").getNodeValue();
            if (!id.equals(currency.getId())) continue;
            Case newCase = new Case();
            newCase.setDate(date);
            newCase.setCurrency(currency.getISO_Char_Code());
            NodeList nodeList = node.getChildNodes();
            Node nodeVal = nodeList.item(4);
            newCase.setValue(Double.parseDouble(nodeVal.getFirstChild().getNodeValue().replaceAll(",", ".")));
            Node nodeNominal = nodeList.item(2);
            newCase.setNominal(Integer.parseInt(nodeNominal.getFirstChild().getNodeValue()));
            caseList.add(0, newCase);
            break;
        }
//        caseList.forEach(System.out::println);
//        System.out.println("-------------------------------");
    }

    private URLConnection getUrlConnection(LocalDate localDate) throws IOException {
        int m = localDate.getMonthValue();
        int d = localDate.getDayOfMonth();
        String month = m < 10 ? "0" + m : Integer.toString(m);
        String day = d < 10 ? "0" + d : Integer.toString(d);

        String urlString = String.format("http://www.cbr-xml-daily.ru/archive/%d/%s/%s/daily.xml",
                localDate.getYear(), month, day);
//        System.out.println(urlString);
        URL url = new URL(urlString);
        log.info("Подключение к {}", urlString);
        return url.openConnection();
    }
}
