package ru.liga.algoritms;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import ru.liga.helpers.DateHelper;
import ru.liga.model.Case;
import ru.liga.model.Request;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineRegression extends Algo {
    @Override
    public List<Case> getPrediction(List<Case> data, Request request) {
        //создаю новый лист чтобы не вносить изменения в предыдущий
        List<Case> newData = new ArrayList<>(data);
        LocalDate today = LocalDate.now();
        LocalDate lastDayInList = newData.get(0).getDate();//последний известный день
        int countDaysForPredict = DateHelper.getCountDays(request);

        //добавляю количество дней до сегодня + количество дней для прогноза
        LocalDate stopDay = lastDayInList.plusDays(countDaysForPredict
                + (int) ChronoUnit.DAYS.between(lastDayInList, today));
//        если только от последнего известного дня
//        LocalDate stopDay = lastDayInList.plusDays(countDaysForPredict);

        //сб и вс пропускаются поэтому надо добавить еще 2 дня для прогноза
        if (countDaysForPredict == 7) stopDay = stopDay.plusDays(2);
//        if (countDaysForPredict == 30) stopDay = stopDay.plusDays(8);
        //добавляем новые значения пока не достигнем целевого дня
        while (newData.get(0).getDate().isBefore(stopDay.plusDays(1))) {
            double newValue = process(newData);
            addNewCase(newData, newValue);
        }
        newData = newData.subList(0, countDaysForPredict);
        Collections.reverse(newData);
        return newData;
    }
    
    private double process(List <Case> newData){
        SimpleRegression simpleRegression = new SimpleRegression(true);
        //берем последние n значений (в данном случае 7)
        double [][] dataArray = new double[7][2];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i][0] = i;
            dataArray[i][1] = newData.get(i).getValue();
//            System.out.println(i + " " + newData.get(i).getValue());
        }
        simpleRegression.addData(dataArray);
        //Выбор коэффициента(фактора?) по умолчанию 1.5
        return simpleRegression.predict(1.5);
    }
    
//    private double process(List<Case> newData) {
//        Attribute p = new Attribute("price");
//        Attribute d = new Attribute("date");
//        ArrayList<Attribute> attributes = new ArrayList<>();
//        attributes.add(d);
//        attributes.add(p);
//        newData = newData.subList(0, 30);
//        Instances prises = new Instances("prices", attributes, newData.size());
//        prises.setClassIndex(prises.numAttributes() - 1);
//        getNewInstance(newData, p, d, prises);
//        double value = 0;
//        try {
//            Instances testingDataSet = new Instances(prises);
//            Classifier classifier = new LinearRegression();
//            Evaluation eval = new Evaluation(prises);
//            classifier.buildClassifier(prises);
//            eval.evaluateModel(classifier, testingDataSet);
////            System.out.println("** Linear Regression Evaluation with Datasets **");
////            System.out.println(eval.toSummaryString());
////            System.out.print(" the expression for the input newData as per alogorithm is ");
////            System.out.println(classifier);
//            Instances instances = new Instances(prises);
//            Instance predicationDataSet = instances.lastInstance();
//            value = classifier.classifyInstance(predicationDataSet);
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//        return Precision.round(value, 4);

//    }
//
//    private void getNewInstance(List<Case> newData, Attribute p, Attribute d, Instances prises) {
//        for (int j = newData.size() - 1; j >= 0; j--) {
//            Instance instance = new DenseInstance(2);
//            long days = ChronoUnit.DAYS.between(newData.get(j).getDate(), LocalDate.now());
//            instance.setValue(d, days);
//            instance.setValue(p, newData.get(j).getValue());
//            prises.add(instance);
//        }
//    }

}
