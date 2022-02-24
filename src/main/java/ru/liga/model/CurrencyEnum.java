package ru.liga.model;

public enum CurrencyEnum {
    //индекс дает понять CBR по какой валюте нужны данные
    USD("R01235","Доллар США","USD"),
    EUR("R01239","Евро","EUR"),
    TRY("R01700J","Турецкая лира","TRY");

    public String id;
    public String name;
    public String ISO_Char_Code;

    CurrencyEnum(String id, String name, String ISO_Char_Code) {
        this.id = id;
        this.name = name;
        this.ISO_Char_Code = ISO_Char_Code;
    }
}
