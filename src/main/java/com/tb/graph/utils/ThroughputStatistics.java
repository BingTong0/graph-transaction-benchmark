package com.tb.graph.utils;

import java.util.ArrayList;
import java.util.List;

public class ThroughputStatistics {
    private final List<Double> throughputList = new ArrayList<>();

    public static ThroughputStatistics getInstance() {
        return new ThroughputStatistics();
    }

    // get throughput
    public double getAverageThroughput() {
        return throughputList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public void addThroughput(double throughput) {
        throughputList.add(throughput);
    }
}
