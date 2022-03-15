package ru.liga.view;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;
import ru.liga.model.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class Graph {
    private List<List<Case>> allData = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();

    public void addData(List<Case> dataOneCurrency, Request request) {
        allData.add(dataOneCurrency);
        requests.add(request);
    }

    public void draw() throws Exception {
        List<Double> x = NumpyUtils.linspace(0, allData.get(0).size(), allData.get(0).size());
        StringBuilder stringBuilder = new StringBuilder();
        Plot plt = Plot.create();
        for (int i = 0; i < allData.size(); i++) {
            List<Case> data = allData.get(i);
            Request request = requests.get(i);
            String color = Color.getColorById(i%Color.values().length);
            List<Double> C = data.stream().map(c -> c.getValue()).collect(Collectors.toList());//
            plt.plot().add(x, C).color(color).linewidth(2.5).linestyle("-");
            stringBuilder.append(request.getISO_Char_Code()).append("-").append(color).append(" ");
        }
        try {
            plt.title(stringBuilder.toString());
            plt.savefig("graph.png").dpi(200);
            plt.executeSilently();
            log.info("graph.png создан");
        } catch (IOException | PythonExecutionException e) {
            log.info("График не создан");
            throw new Exception("График не создан");
        }
    }
}
