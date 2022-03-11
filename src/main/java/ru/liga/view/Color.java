package ru.liga.view;

public enum Color {
    Red("red",0),
    Blue("blue",1),
    Green("green",2),
    Orange("orange",3),
    Brown("brown", 4);

    String color;
    int idColor;
    Color(String color,int idColor) {
        this.color = color;
        this.idColor = idColor;
    }

    public static String getColorById(int i) {
        return Color.values()[i].color;
    }
}
