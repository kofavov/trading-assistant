package ru.liga.version2.view;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.exception.RequestException;
import ru.liga.version2.model.Case;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Graph {
    private static final String OUTPUT_FILE_NAME = "graph.png";
    private final HashMap<String, TreeMap<LocalDate, Case>> allData;

    public Graph(HashMap<String, TreeMap<LocalDate, Case>> allData) {
        this.allData = allData;
    }

    public void draw() throws Exception {
        Plot plt = Plot.create();
        StringBuilder stringBuilder = new StringBuilder();
        preparePlot(plt, stringBuilder);
        createGraphFile(plt, stringBuilder);
    }

    private void preparePlot(Plot plt, StringBuilder stringBuilder) {
        int numColor = 0;
        for (Map.Entry<String, TreeMap<LocalDate, Case>> cur : allData.entrySet()) {
            List<Case> data = new ArrayList<>(allData.get(cur.getKey()).values());
            List<Double> x = NumpyUtils.linspace(0, data.size(), data.size());
            String color = Color.getColorById(numColor++ % Color.values().length);
            List<Double> casesValue = data.stream().map(Case::getValue).collect(Collectors.toList());//
            plt.plot().add(x, casesValue).color(color).linewidth(2.5).linestyle("-");
            stringBuilder.append(cur.getKey()).append("-").append(color).append(" ");
        }
    }

    private void createGraphFile(Plot plt, StringBuilder stringBuilder) {
        try {
            plt.title(stringBuilder.toString());
            plt.savefig(OUTPUT_FILE_NAME).dpi(200);
            plt.executeSilently();
            log.info("{} создан", OUTPUT_FILE_NAME);
        } catch (IOException | PythonExecutionException e) {
            log.info("График не создан");
            throw new RequestException("График не создан");
        }
    }
}
