package ru.liga.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@EqualsAndHashCode
public class Currency {
    private static final Map<String, Currency> CURRENCY_MAP = new TreeMap<>();
    private String id;
    private String name;
    private String engName;
    private Integer nominal;
    private String parentCode;
    private Integer ISO_Num_Code;
    private String ISO_Char_Code;

    static {
        try {
            fillMap();
        } catch (Exception e) {
            System.out.println("Данные из ЦБ недоступны Будут использоваться локальные");
            System.out.println(e.getMessage());
            CURRENCY_MAP.put("USD", new Currency(ru.liga.model.CurrencyEnum.USD));
            CURRENCY_MAP.put("EUR", new Currency(ru.liga.model.CurrencyEnum.EUR));
            CURRENCY_MAP.put("TRY", new Currency(ru.liga.model.CurrencyEnum.TRY));
        }
    }

    public Currency(ru.liga.model.CurrencyEnum currencyEnum) {
        this.id = currencyEnum.id;
        this.name = currencyEnum.name;
        this.ISO_Char_Code = currencyEnum.ISO_Char_Code;
        this.nominal = currencyEnum.nominal;
    }

    public Currency(String id, String name, String engName, Integer nominal,
                    String parentCode, Integer ISO_Num_Code, String ISO_Char_Code) {
        this.id = id;
        this.name = name;
        this.engName = engName;
        this.nominal = nominal;
        this.parentCode = parentCode;
        this.ISO_Num_Code = ISO_Num_Code;
        this.ISO_Char_Code = ISO_Char_Code;
    }

    private static void fillMap() throws Exception {
        URLConnection connection = getURLConnectionAllCurrencies();
        connection.setConnectTimeout(1500);
        connection.setReadTimeout(1500);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(connection.getInputStream());

        NodeList curElements = doc.getDocumentElement().getElementsByTagName("Valute");
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            String id = attributes.getNamedItem("ID").getNodeValue();
            NodeList nodeList = node.getChildNodes();
            //несколько валют имеют не полные данные их надо пропустить
            if (id.equals("R01720A") || id.equals("R01436")) continue;
            Currency currency = getNewCurrency(nodeList, id);
            CURRENCY_MAP.put(currency.getISO_Char_Code(), currency);
        }
    }

    private static Currency getNewCurrency(NodeList nodeList, String id) {
        Integer ISO_Num_Code = Integer.valueOf(nodeList.item(0).getFirstChild().getNodeValue());
        String ISO_Char_Code = nodeList.item(1).getFirstChild().getNodeValue();
        Integer nominal = Integer.parseInt(nodeList.item(2).getFirstChild().getNodeValue());
        String name = nodeList.item(3).getFirstChild().getNodeValue();
        String parentCode = "";
        String engName = "";
        return new Currency(id, name, engName, nominal, parentCode, ISO_Num_Code, ISO_Char_Code);
    }

    private static URLConnection getURLConnectionAllCurrencies() throws IOException {
        URL url = new URL("http://www.cbr-xml-daily.ru/daily.xml");
        return url.openConnection();
    }

    public static Map<String, Currency> getCurrencyMap() {
        return CURRENCY_MAP;
    }

    @Override
    public String toString() {
        return ISO_Char_Code + " " + name + " nominal = " + nominal;
    }

}
