package ru.liga.version2.parser;


import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.liga.version2.helper.DateHelper;
import ru.liga.version2.model.Case;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

@Slf4j
public class CBRFExchange implements Parser {
    private final HashMap<String, TreeMap<LocalDate, Case>> data = new HashMap<>();
    private final int countDownloadDays = 10;

    @Override
    public HashMap<String, TreeMap<LocalDate, Case>> getData() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        log.info("Получение данных из ЦБ");
        //количество загружаемых дней нельзя больше 119
        for (int i = 0; i < countDownloadDays; i++) {
            LocalDate tempDay = tomorrow.minusDays(i);
            if (DateHelper.checkWeekend(tempDay)) {
                continue;
            }
            try {
                getDataFromXml(tempDay);
            } catch (IOException | ParserConfigurationException e) {
                log.info(e.getMessage());
            }
            try {
                Thread.sleep(210);
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }
        return data;
    }

    public Case getDataForDay(LocalDate date, String iso)  {
        log.info("Получение данных из ЦБ");

        try {
            getDataFromXml(date);
        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(210);
        } catch (InterruptedException ignored) {
        }

        return data.get(iso).get(date);
    }

    private void getDataFromXml(LocalDate tempDay) throws IOException, ParserConfigurationException {
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
        getCases(curElements, tempDay);
    }

    private void getCases(NodeList curElements, LocalDate date) {
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            Case newCase = new Case(node, date);
            if (!data.containsKey(newCase.getISOCharCode())) {
                data.put(newCase.getISOCharCode(), new TreeMap<>());
            }
            data.get(newCase.getISOCharCode()).put(date, newCase);
        }
    }

    private URLConnection getUrlConnection(LocalDate localDate) throws IOException {
        int m = localDate.getMonthValue();
        int d = localDate.getDayOfMonth();
        String month = m < 10 ? "0" + m : Integer.toString(m);
        String day = d < 10 ? "0" + d : Integer.toString(d);

        String urlString = String.format("http://www.cbr-xml-daily.ru/archive/%d/%s/%s/daily.xml",
                localDate.getYear(), month, day);
        URL url = new URL(urlString);
        log.info("Подключение к {}", urlString);
        return url.openConnection();
    }
}
