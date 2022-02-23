package ru.liga.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.liga.helpers.IdCurrencyForCBR;
import ru.liga.model.Case;

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

public class OnlineCBRExchange implements Parser{

    public static List<Case> getData(String[] request)  {
        List<Case> caseList = new ArrayList<>();
        String cur = IdCurrencyForCBR.valueOf(request[1]).getCurrency();

        try {
            URLConnection conn = getUrlConnection(cur);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList curElements = doc.getDocumentElement().getElementsByTagName("Record");
            getCases(request[1], caseList, curElements);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return caseList;
    }

    private static void getCases(String currency, List<Case> caseList, NodeList curElements) {
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            Case c = new Case();
            DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            c.setDate(LocalDate.parse(attributes.getNamedItem("Date").getNodeValue(),dateTimeFormatter1));
            c.setCurrency(currency);
            NodeList nodeList = node.getChildNodes();
            Node node1 = nodeList.item(1);
            c.setValue(Double.parseDouble(node1.getFirstChild().getNodeValue().replaceAll(",",".")));
            caseList.add(0,c);
        }
//        caseList.forEach(System.out::println);
//        System.out.println("-------------------------------");
    }

    private static URLConnection getUrlConnection(String cur) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate weekAgo = LocalDate.now().minusDays(90);
        LocalDate now = LocalDate.now();

        String u = String.format("http://www.cbr.ru/scripts/XML_dynamic.asp" +
                "?date_req1=%s&date_req2=%s&VAL_NM_RQ=%s",
                weekAgo.format(dateTimeFormatter),now.format(dateTimeFormatter), cur);
//        System.out.println(u);
        URL url = new URL(u);
        URLConnection conn = url.openConnection();
        return conn;
    }


}
