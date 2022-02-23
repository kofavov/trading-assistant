package ru.liga.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.liga.model.Currency;
import ru.liga.model.IdCurrencyForCBR;
import ru.liga.model.Case;
import ru.liga.model.Request;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CBRFExchange implements Parser{

    public static List<Case> getData(Request request)  {
        List<Case> data = new ArrayList<>();
        Currency currency = Currency.getCurrencyHashMap().get(request.getISO_Char_Code());

        try {
            URLConnection conn = getUrlConnection(currency.getId());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList curElements = doc.getDocumentElement().getElementsByTagName("Record");
            getCases(currency.getName(), data, curElements);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void getCases(String currency, List<Case> caseList, NodeList curElements) {
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            Case newCase = new Case();
            DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            //почему-то данные из цбрф с вторника по субботу
            //видимо окончательная цена записывается на следующий день
            newCase.setDate(LocalDate.parse(attributes.getNamedItem("Date").getNodeValue(),dateTimeFormatter1));
            newCase.setCurrency(currency);
            NodeList nodeList = node.getChildNodes();
            Node node1 = nodeList.item(1);
            newCase.setValue(Double.parseDouble(node1.getFirstChild().getNodeValue().replaceAll(",",".")));
            caseList.add(0,newCase);
        }
//        caseList.forEach(System.out::println);
//        System.out.println("-------------------------------");
    }

    private static URLConnection getUrlConnection(String id) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dayInThePast = LocalDate.now().minusDays(90);
        LocalDate now = LocalDate.now();

        String urlString = String.format("http://www.cbr.ru/scripts/XML_dynamic.asp" +
                "?date_req1=%s&date_req2=%s&VAL_NM_RQ=%s",
                dayInThePast.format(dateTimeFormatter),now.format(dateTimeFormatter), id);
        URL url = new URL(urlString);
        return url.openConnection();
    }






}
