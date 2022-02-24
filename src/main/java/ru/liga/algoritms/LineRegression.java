package ru.liga.algoritms;

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
        //добавляем новые значения пока не достигнем целевого дня
        while (newData.get(0).getDate().isBefore(stopDay.plusDays(1))) {
            double newValue = process(newData);
            addNewCase(newData, newValue);
        }
        newData = newData.subList(0, countDaysForPredict);
        Collections.reverse(newData);
        return newData;
    }


    private double process(List<Case> newData) {
        Attribute p = new Attribute("price");
        Attribute d = new Attribute("date");
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(d);
        attributes.add(p);
        newData = newData.subList(0, 30);
        Instances prises = new Instances("prices", attributes, newData.size());
        prises.setClassIndex(prises.numAttributes() - 1);
        getNewInstance(newData, p, d, prises);
        double value = 0;
        try {
            Instances testingDataSet = new Instances(prises);
            Classifier classifier = new LinearRegression();
            Evaluation eval = new Evaluation(prises);
            classifier.buildClassifier(prises);
            eval.evaluateModel(classifier, testingDataSet);
//            System.out.println("** Linear Regression Evaluation with Datasets **");
//            System.out.println(eval.toSummaryString());
//            System.out.print(" the expression for the input newData as per alogorithm is ");
//            System.out.println(classifier);
            Instances instances = new Instances(prises);
            Instance predicationDataSet = instances.lastInstance();
            value = classifier.classifyInstance(predicationDataSet);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return Precision.round(value, 4);
    }

    private void getNewInstance(List<Case> newData, Attribute p, Attribute d, Instances prises) {
        for (int j = newData.size() - 1; j >= 0; j--) {
            Instance instance = new DenseInstance(2);
            long days = ChronoUnit.DAYS.between(newData.get(j).getDate(), LocalDate.now());
            instance.setValue(d, days);
            instance.setValue(p, newData.get(j).getValue());
            prises.add(instance);
        }
    }


    //    @Override
//    public List<Case> getPrediction(List<Case> data, String request) {
//
//        LocalDate today = LocalDate.now();
//        LocalDate lastDayInList = data.get(0).getDate();
//        int days = 0;
//        String[] param = request.split(" ");
//        if (param[2].equals("tomorrow")) {
//            days = 1;
//        } else if (param[2].equals("week")) {
//            days = 7;
//        }
//        int i = days;
////        int i = days + (int) ChronoUnit.DAYS.between(lastDayInList, today);
//        ArrayList<Case> futureCases = new ArrayList<>();
//        while (i != 0) {
//            Attribute p = new Attribute("price");
////            Attribute d = new Attribute("date");
//            ArrayList<Attribute> attributes = new ArrayList<>();
//            attributes.add(p);
//            data = data.subList(0, 50);
//            Instances prises = new Instances("prices", attributes, data.size());
//            prises.setClassIndex(prises.numAttributes() - 1);
//            for (int j = 0; j < data.size(); j++) {
//                Instance instance = new DenseInstance(1);
//                instance.setValue(p, data.get(j).getValue());
//                prises.add(instance);
//            }
//            Classifier targetFunction = new LinearRegression();
//            try {
//                targetFunction.buildClassifier(prises);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////            System.out.printf("model parameters: %s%n", targetFunction);
//            // Now Predicting the cost
//            var lastInstance = prises.lastInstance();
//            double price = 0;
//            try {
//                price = targetFunction.classifyInstance(lastInstance);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Case currentPoint = data.get(data.size() - 1);
//            Case futurePoint = new Case();
//            futurePoint.setDate(currentPoint.getDate().plusDays(1));
//            futurePoint.setCurrency(currentPoint.getCurrency());
//            futurePoint.setValue(Precision.round(price, 4));
//            data.add(0,futurePoint);
//            futureCases.add(0, futurePoint);
//            i--;
//        }
//        return futureCases;

//    }
//    public static void lineRegression(String request) throws Exception {
//        String currency = request.split(" ")[1];
//
//        //Load Data set
//       Instances data =  new Instances(loadDataFromCsvFile(currency+".csv"));
//
//        data.setClassIndex(data.numAttributes() - 1);
//        //Build model
//        LinearRegression model = new LinearRegression();
//        try { model.buildClassifier(data); }
//        catch (Exception e) { e.printStackTrace(); }
//        //output model
//        System.out.printf("model parameters: %s%n", model);
//        // Now Predicting the cost
//        var myHouse = data.lastInstance();
//        var price  = model.classifyInstance(myHouse);
//        System.out.printf("predicted price = %s%n", price);
//    }
//
//    public static Instances loadDataFromCsvFile(String path) throws IOException {
//        CSVLoader loader = new CSVLoader();
//        loader.setFieldSeparator(";");
//
//        loader.setSource(new File(path+".csv"));
//
//        Instances data = loader.getDataSet();
//        data.deleteAttributeAt(2);
//        data.deleteAttributeAt(0);
//
////          System.out.println("\nHeader of dataset:\n");
////        System.out.println(new Instances(data, 0));
//        return data;

//    }
}
