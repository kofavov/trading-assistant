package ru.liga.model;

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
import java.util.Objects;
import java.util.TreeMap;

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
            CURRENCY_MAP.put("USD", new Currency(CurrencyEnum.USD));
            CURRENCY_MAP.put("EUR", new Currency(CurrencyEnum.EUR));
            CURRENCY_MAP.put("TRY", new Currency(CurrencyEnum.TRY));
        }
    }

    public Currency(CurrencyEnum currencyEnum) {
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

        NodeList curElements = doc.getDocumentElement().getElementsByTagName("Item");
        for (int i = 0; i < curElements.getLength(); i++) {
            Node node = curElements.item(i);
            NamedNodeMap attributes = node.getAttributes();
            String id = attributes.getNamedItem("ID").getNodeValue();
            NodeList nodeList = node.getChildNodes();
            //несколько валют имеют не полные данные их надо пропустить
            if (id.equals("R01720A") || id.equals("R01436")) continue;
            Currency currency = getNewCurrency(nodeList, id);
            CURRENCY_MAP.put(currency.ISO_Char_Code, currency);
        }
    }

    private static Currency getNewCurrency(NodeList nodeList, String id) {
        String name = nodeList.item(0).getFirstChild().getNodeValue();
        String engName = nodeList.item(1).getFirstChild().getNodeValue();
        Integer nominal = Integer.parseInt(nodeList.item(2).getFirstChild().getNodeValue());
        String parentCode = nodeList.item(3).getFirstChild().getNodeValue();
        Integer ISO_Num_Code = Integer.valueOf(nodeList.item(4).getFirstChild().getNodeValue());
        String ISO_Char_Code = nodeList.item(5).getFirstChild().getNodeValue();
        return new Currency(id, name, engName, nominal, parentCode, ISO_Num_Code, ISO_Char_Code);
    }

    private static URLConnection getURLConnectionAllCurrencies() throws IOException {
        URL url = new URL("http://www.cbr.ru/scripts/XML_valFull.asp");
        return url.openConnection();
    }

    public static Map<String, Currency> getCurrencyMap() {
        return CURRENCY_MAP;
    }

    @Override
    public String toString() {
        return ISO_Char_Code + " " + name + " nominal = " + nominal ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getISO_Num_Code() {
        return ISO_Num_Code;
    }

    public void setISO_Num_Code(int ISO_Num_Code) {
        this.ISO_Num_Code = ISO_Num_Code;
    }

    public String getISO_Char_Code() {
        return ISO_Char_Code;
    }

    public void setISO_Char_Code(String ISO_Char_Code) {
        this.ISO_Char_Code = ISO_Char_Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(nominal, currency.nominal)
                && Objects.equals(ISO_Num_Code, currency.ISO_Num_Code)
                && Objects.equals(id, currency.id)
                && Objects.equals(name, currency.name)
                && Objects.equals(engName, currency.engName)
                && Objects.equals(parentCode, currency.parentCode)
                && Objects.equals(ISO_Char_Code, currency.ISO_Char_Code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, engName, nominal, parentCode, ISO_Num_Code, ISO_Char_Code);
    }
}
