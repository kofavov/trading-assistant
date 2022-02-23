package ru.liga.model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Currency {
    private static final HashMap<String, Currency> CURRENCY_HASH_MAP = new HashMap<>();
    private String id;
    private String name;
    private String engname;
    private Integer nominal;
    private String parentCode;
    private Integer ISO_Num_Code;
    private String ISO_Char_Code;

    static {
        fillMap();
    }

    public Currency(String id, String name, String engname, Integer nominal, String parentCode, Integer ISO_Num_Code, String ISO_Char_Code) {
        this.id = id;
        this.name = name;
        this.engname = engname;
        this.nominal = nominal;
        this.parentCode = parentCode;
        this.ISO_Num_Code = ISO_Num_Code;
        this.ISO_Char_Code = ISO_Char_Code;
    }

    private static void fillMap() {
        try {
            URLConnection connection = getURLConnectionAllCurrencies();
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
                String name = nodeList.item(0).getFirstChild().getNodeValue();
                String engname = nodeList.item(1).getFirstChild().getNodeValue();
                Integer nominal = Integer.parseInt(nodeList.item(2).getFirstChild().getNodeValue());
                String parentCode = nodeList.item(3).getFirstChild().getNodeValue();
                Integer ISO_Num_Code = Integer.valueOf(nodeList.item(4).getFirstChild().getNodeValue());
                String ISO_Char_Code = nodeList.item(5).getFirstChild().getNodeValue();
                Currency currency = new Currency(id, name, engname, nominal, parentCode, ISO_Num_Code, ISO_Char_Code);
                CURRENCY_HASH_MAP.put(ISO_Char_Code, currency);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static URLConnection getURLConnectionAllCurrencies() throws IOException {
        URL url = new URL("http://www.cbr.ru/scripts/XML_valFull.asp");
        URLConnection conn = url.openConnection();
        return conn;
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

    public String getEngname() {
        return engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
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
        return nominal == currency.nominal && ISO_Num_Code == currency.ISO_Num_Code && Objects.equals(id, currency.id) && Objects.equals(name, currency.name) && Objects.equals(engname, currency.engname) && Objects.equals(parentCode, currency.parentCode) && Objects.equals(ISO_Char_Code, currency.ISO_Char_Code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, engname, nominal, parentCode, ISO_Num_Code, ISO_Char_Code);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", engname='" + engname + '\'' +
                ", nominal=" + nominal +
                ", parentCode='" + parentCode + '\'' +
                ", ISO_Num_Code=" + ISO_Num_Code +
                ", ISO_Char_Code='" + ISO_Char_Code + '\'' +
                '}';
    }

    public static HashMap<String, Currency> getCurrencyHashMap() {
        return CURRENCY_HASH_MAP;
    }
}
