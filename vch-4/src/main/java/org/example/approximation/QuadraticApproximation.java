package org.example.approximation;

import org.example.util.LinearSystemSolver;
import org.example.util.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class QuadraticApproximation extends Approximation {

    public QuadraticApproximation(List<double[]> points) {
        super(points);
        calculateCoefficients();
    }

    @Override
    void calculateCoefficients() {
        double[][] a = {{n(), sx(), sxx()}, {sx(), sxx(), sxxx()}, {sxx(), sxxx(), sxxxx()}};
        double[] b = {sy(), sxy(), sxxy()};
        double[] coefficients = LinearSystemSolver.solve(a, b);

        if (coefficients.length == 0) {
            setCorrect(false);
            return;
        }

        getCoefficients().put("a0", coefficients[0]);
        getCoefficients().put("a1", coefficients[1]);
        getCoefficients().put("a2", coefficients[2]);
    }

    @Override
    public double calculateValue(double x) {
        return getCoefficients().get("a0") + getCoefficients().get("a1") * x + getCoefficients().get("a2") * Math.pow(x, 2);
    }

    @Override
    public String getName() {
        return "КВАДРАТИЧНАЯ АППРОКСИМАЦИЯ";
    }


    public ArrayList<Object> ans() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(getName());
        if (!isCorrect()) {
            res.add("На основе введенных данных не удалось построить квадратичную аппроксимацию.");
            return res;
        }
        res.add("Полученная аппроксимирующая функция: y = " + getCoefficients().get("a0") + " + " + getCoefficients().get("a1") + "x + " + getCoefficients().get("a2") + "x^2");

        List<String> headers = List.of("№ п.п.", "X", "Y", "y=a0+a1x+a2x^2", "εi");
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
        cof.add(getCoefficients().get("a0"));
        cof.add(getCoefficients().get("a1"));
        cof.add(getCoefficients().get("a2"));
        return cof;
    }
}