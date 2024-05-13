package org.example.approximation;

import org.example.util.LinearSystemSolver;
import org.example.util.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class LogarithmicApproximation extends Approximation {
    
    public LogarithmicApproximation(List<double[]> points) {
        super(points);
        calculateCoefficients();
    }

    @Override
    void calculateCoefficients() {
        for (double[] point : getPoints()) {
            if (point[0] <= 0) {
                setCorrect(false);
                return;
            }
        }        

        double[][] a = {{sXX(), sX()}, {sX(), n()}};
        double[] b = {sXy(), sy()};
        double[] coefficients = LinearSystemSolver.solve(a, b);

        if (coefficients.length == 0) {
            setCorrect(false);
            return;
        }

        getCoefficients().put("a", coefficients[0]);
        getCoefficients().put("b", coefficients[1]);
    }

    @Override
    public double calculateValue(double x) {
        return getCoefficients().get("a") * Math.log(x) + getCoefficients().get("b");
    }

    @Override
    public String getName() {
        return "ЛОГАРИФМИЧЕСКАЯ АППРОКСИМАЦИЯ";
    }


    public ArrayList<Object> ans() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(getName());
        if (!isCorrect()) {
            res.add("На основе введенных данных не удалось построить логарифмическую аппроксимацию.");
            return res;
        }
        res.add("Полученная аппроксимирующая функция: y = " + getCoefficients().get("a") + "ln(x) + " + getCoefficients().get("b"));

        List<String> headers = List.of("№ п.п.", "X", "Y", "y=aln(x)+b", "εi");
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
