package ru.liga.view;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.github.sh0nk.matplotlib4j.builder.HistBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.liga.model.Case;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
public class Graph {

    public void draw(List<Case> data){
        List<Double> x = NumpyUtils.linspace(0, data.size(), data.size());
        List<Double> C = data.stream().map(c->c.getValue()).collect(Collectors.toList());

        Plot plt = Plot.create();
        plt.plot().add(x, C).color("blue").linewidth(2.5).linestyle("-");

        try {
            plt.title(data.get(0).getCurrency());
            plt.savefig("graph.png").dpi(200);
            plt.executeSilently();
        } catch (IOException | PythonExecutionException e) {
            e.printStackTrace();
        }
    }
}
