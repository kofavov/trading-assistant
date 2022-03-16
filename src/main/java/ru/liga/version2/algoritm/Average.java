package ru.liga.version2.algoritm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.version2.model.Case;

import java.util.*;

@Slf4j
public class Average extends SimpleAlgo {

    @Override
    protected double getValue(List<Case> data) {
        return data.subList(0, 7).stream().mapToDouble(Case::getValue).average().orElseThrow();
    }

}
