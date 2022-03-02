package ru.liga.parsers;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class CBRFExchange implements Parser {
    public List<Case> getData(Request request) throws Exception {
        List<Case> data = new ArrayList<>();
        //возможно есть инфа на завтра
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int historyTimeFrame = 7;//сколько дней загружаем
        Currency currency = Currency.getCurrencyMap().get(request.getISO_Char_Code());
        System.out.println("Выполнение запроса " + request + " к ЦБ ");
        for (int i = 0; data.size() != historyTimeFrame; i++) {
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
            URLConnection connection = getUrlConnection(tempDay);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            try {
                //Возникает Exception если на данный день нет данных(например выходной или праздник)
                doc = builder.parse(connection.getInputStream());
            } catch (Exception e) {
                continue;
            }
            NodeList curElements = doc.getDocumentElement().getElementsByTagName("Valute");
            getCases(currency, data, curElements, tempDay);

        }
        Collections.reverse(data);
        return data;
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
            Node node1 = nodeList.item(4);
            newCase.setValue(Double.parseDouble(node1.getFirstChild().getNodeValue().replaceAll(",", ".")));
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
        log.info("Подключение к {}",urlString);
        return url.openConnection();
    }


}
