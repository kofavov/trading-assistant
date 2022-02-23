package ru.liga.helpers;

public enum IdCurrencyForCBR {
    //индекс дает понять CBR по какой валюте нужны данные
    USD("R01235"),EUR("R01239"),TRY("R01700J");

    private String currency;

    IdCurrencyForCBR(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
