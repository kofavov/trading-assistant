package ru.liga.version2.view;

public enum Color {
    Red("red"),
    Blue("blue"),
    Green("green"),
    Orange("orange"),
    Brown("brown"),
    Yellow("yellow"),
    Purple("purple");

    String color;

    Color(String color) {
        this.color = color;
    }

    public static String getColorById(int i) {
        return Color.values()[i].color;
    }
}
