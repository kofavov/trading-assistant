package ru.liga.version2.algoritm;

import ru.liga.version2.exception.RequestException;

public class AlgoritmFactory {
    public Algoritm getAlgoritm(String algoCommand) throws RequestException {
        switch (algoCommand) {
            case "lr":
                return new LineRegression();
            case "lri":
                return new LineRegressionFromInet();
            case "act":
                return new Actual();
            case "moon":
                return new Moon();
            case "avg":
                return new Average();
            default:
                throw new RequestException("алгоритм не найден");
        }
    }
}
