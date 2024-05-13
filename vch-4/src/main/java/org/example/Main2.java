package org.example;

import org.example.approximation.*;
import org.example.util.*;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main2 {
    public ArrayList<ArrayList<Double>> dd = new ArrayList<>();
    public ArrayList<Object> start(HashMap point) {
        boolean readFromFile = false, writeToFile = false;
        File source = null, target = null;
        ArrayList<Object> finalAns = new ArrayList<>();
        List<Double> values = new ArrayList<>(point.values());
        List<String> keys = new ArrayList<>(point.keySet());
        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < keys.size(); i += 2) {
            double[] pair = new double[2];
            Object value1 = values.get(i + 1);
            Object value2 = values.get(i);
            pair[0] = value1 instanceof Integer ? ((Integer) value1).doubleValue() : ((Double) value1).doubleValue();
            pair[1] = value2 instanceof Integer ? ((Integer) value2).doubleValue() : ((Double) value2).doubleValue();
            points.add(pair);
        }

        //List<double[]> points = null;
        String res;

        Approximation linearApproximation = new LinearApproximation(points);
        Approximation quadraticApproximation = new QuadraticApproximation(points);
        Approximation powerApproximation = new PowerApproximation(points);
        Approximation exponentialApproximation = new ExponentialApproximation(points);
        Approximation logarithmicApproximation = new LogarithmicApproximation(points);
        finalAns.add(linearApproximation.ans());
        finalAns.add(quadraticApproximation.ans());
        finalAns.add(powerApproximation.ans());
        finalAns.add(exponentialApproximation.ans());
        finalAns.add(logarithmicApproximation.ans());
        dd.add(linearApproximation.coefic());
        dd.add(quadraticApproximation.coefic());
        dd.add(powerApproximation.coefic());
        dd.add(exponentialApproximation.coefic());
        dd.add(logarithmicApproximation.coefic());
        return finalAns;
    }
    public ArrayList<ArrayList<Double>> functions() {
        return dd;
    }
    public void remove() {
        dd.clear();
    }
}