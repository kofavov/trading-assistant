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

public class Graph {

    public void draw(List<Case> data){
        List<Double> x = NumpyUtils.linspace(0, data.size(), data.size());
        List<Double> C = data.stream().map(c->c.getValue()).collect(Collectors.toList());
//        List<Double> S = x.stream().map(xi -> Math.sin(xi)).collect(Collectors.toList());

        Plot plt = Plot.create();
        plt.plot().add(x, C).color("blue").linewidth(2.5).linestyle("-");
//        plt.plot().add(x, S).color("red").linewidth(2.5).linestyle("-");

        try {
            plt.savefig("graph.png").dpi(200);
            plt.executeSilently();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }
    }
}
