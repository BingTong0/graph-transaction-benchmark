package com.tb.graph.utils;

import java.util.ArrayList;
import java.util.List;

public class ResponseTimeStatistics {
    private final List<Long> responseTimeList = new ArrayList<>();

    public static ResponseTimeStatistics getInstance() {
        return new ResponseTimeStatistics();
    }

    // get response time
    public double getAverageResponseTime() {
        return responseTimeList.stream().mapToLong(Long::longValue).average().orElse(0.0);
    }

    public void addResponseTime(long responseTime) {
        responseTimeList.add(responseTime);
    }
}
