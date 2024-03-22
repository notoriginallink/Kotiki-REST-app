package ru.tolstov;

import java.util.Map;
import java.util.TreeMap;

public class DepositPercentageManager {
    private final Map<Double, Double> percents;
    public DepositPercentageManager() {
        percents = new TreeMap<>();
        percents.put(0d, 0d);
    }

    public boolean add(double value, double percent) {
        return percents.put(value, percent) == null;
    }

    public boolean remove(double value) {
        return percents.remove(value) != null;
    }

    public double getPercentByValue(double value) {
        double percent = 0;
        for (var pair : percents.entrySet()) {
            if (pair.getKey() <= value)
                percent = pair.getValue();
            else
                break;
        }

        return percent;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (var pair : percents.entrySet())
            builder.append("%s | %s\n".formatted(pair.getKey(), pair.getValue()));

        return builder.toString();
    }
}
