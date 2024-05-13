package org.example.approximation;

import org.example.util.*;


import java.util.ArrayList;
import java.util.List;

public class PowerApproximation extends Approximation {

    public PowerApproximation(List<double[]> points) {
        super(points);
        calculateCoefficients();
    }

    @Override
    void calculateCoefficients() {
        for (double[] point : getPoints()) {
            if (point[0] <= 0 || point[1] <= 0) {
                setCorrect(false);
                return;
            }
        }

        double[][] a = {{sXX(), sX()}, {sX(), n()}};
        double[] b = {sXY(), sY()};
        double[] coefficients = LinearSystemSolver.solve(a, b);

        if (coefficients.length == 0) {
            setCorrect(false);
            return;
        }

        getCoefficients().put("a", Math.exp(coefficients[1]));
        getCoefficients().put("b", coefficients[0]);
    }

    @Override
    public double calculateValue(double x) {
        return getCoefficients().get("a") * Math.pow(x, getCoefficients().get("b"));
    }

    @Override
    public String getName() {
        return "СТЕПЕННАЯ АППРОКСИМАЦИЯ";
    }


    public ArrayList<Object> ans() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(getName());
        if (!isCorrect()) {
            res.add("На основе введенных данных не удалось построить степенную аппроксимацию.");
            return res;
        }

        res.add("Полученная аппроксимирующая функция: y = " + getCoefficients().get("a") + "x^" + getCoefficients().get("b"));

        List<String> headers = List.of("№ п.п.", "X", "Y", "y=ax^x", "εi");
        List<List<String>> data = new ArrayList<>();
        List<String> column;
        for (int i = 0; i < getPoints().size(); i++) {
            column = new ArrayList<>();
            double[] point = getPoints().get(i);
            column.add(String.format("%d", (i+1)));
            column.add(String.format("%f", point[0]));
            column.add(String.format("%f", point[1]));
            column.add(String.format("%f", calculateValue(point[0])));
            column.add(String.format("%f", calculateValue(point[0]) - point[1]));
            data.add(column);
        }
        res.add(headers);
        res.add(data);

        res.add("Среднеквадратичное отклонение: 𝜹 = " + calculateStandartDeviation());
        res.add("Коэффициент детерминации: R^2 = " + calculateDeterminationCoefficient() + " - " + getDeterminationCoefficientMessage(calculateDeterminationCoefficient()));
        res.add("");
        return res;
    }
    public ArrayList<Double> coefic() {
        ArrayList<Double> cof = new ArrayList<>();
        cof.add(getCoefficients().get("a"));
        cof.add(getCoefficients().get("b"));
        return cof;
    }
    
}
