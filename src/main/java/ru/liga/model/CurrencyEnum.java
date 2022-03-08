package ru.liga.model;

public enum CurrencyEnum {
    //индекс дает понять CBR по какой валюте нужны данные
    USD("R01235", "Доллар США", "USD", 1),
    EUR("R01239", "Евро", "EUR", 1),
    TRY("R01700J", "Турецкая лира", "TRY", 1),
    AMD("","Армянский драм","AMD",100),
    BGN("","Болгарский лев","BGN",1);

    String id;
    String name;
    String ISO_Char_Code;
    int nominal;

    CurrencyEnum(String id, String name, String ISO_Char_Code, int nominal) {
        this.id = id;
        this.name = name;
        this.ISO_Char_Code = ISO_Char_Code;
        this.nominal = nominal;
    }
}
