package com.tb.graph;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.tb.graph.utils.Charter;
import com.tb.graph.utils.ResponseTimeStatistics;
import com.tb.graph.utils.ThroughputStatistics;

/**
 * compare for different graph database
 * 1. run OLTP & OLAP at the same time
 * 2. only run OLTP
 * 3. only run OLAP
 */
public class Driver {
    public static void compareDatabases(GraphDatabaseBenchmark... databases) {
        XYSeriesCollection throughputDataset = new XYSeriesCollection();
        XYSeriesCollection responseTimeDataset = new XYSeriesCollection();
        DefaultCategoryDataset OLTPdataset = new DefaultCategoryDataset();
        DefaultCategoryDataset OLAPdataset = new DefaultCategoryDataset();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (GraphDatabaseBenchmark database : databases) {
            XYSeries throughputSingleSeries = new XYSeries(database.getClass().getSimpleName() + " OLTP");
            XYSeries responseTimeSingleSeries = new XYSeries(database.getClass().getSimpleName() + " OLAP");
            XYSeries throughputParallelSeries = new XYSeries(database.getClass().getSimpleName() + " OLTP + OLAP");
            XYSeries responseTimeParallelSeries = new XYSeries(database.getClass().getSimpleName() + " OLTP + OLAP");

            ThroughputStatistics oltpThroughputStatistic = ThroughputStatistics.getInstance();
            ThroughputStatistics htapThroughputStatistic = ThroughputStatistics.getInstance();
            ResponseTimeStatistics olapResponseTimeStatistics = ResponseTimeStatistics.getInstance();
            ResponseTimeStatistics htapResponseTimeStatistics = ResponseTimeStatistics.getInstance();

            database.connect();
            // run 5 times
            for (int i = 0; i < 5; i++) {
                // Run OLTP in one thread
                Future<Double> oltp = executorService.submit(database::measureOLTPThroughput);
                // Run OLAP in another thread
                Future<Long> olap = executorService.submit(database::measureOLAPResponseTime);

                try {
                    Double htapThroughput = oltp.get();
                    Long htapResponseTime = olap.get();
                    htapThroughputStatistic.addThroughput(htapThroughput);
                    throughputParallelSeries.add(i, htapThroughput);
                    htapResponseTimeStatistics.addResponseTime(htapResponseTime);
                    responseTimeParallelSeries.add(i, htapResponseTime);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                // only run oltp
                Double oltpThroughput = database.measureOLTPThroughput();
                oltpThroughputStatistic.addThroughput(oltpThroughput);
                throughputSingleSeries.add(i, oltpThroughput);

                // only run olap
                long olapResponseTime = database.measureOLAPResponseTime();
                olapResponseTimeStatistics.addResponseTime(olapResponseTime);
                responseTimeSingleSeries.add(i, olapResponseTime);
            }

            OLTPdataset.addValue(htapThroughputStatistic.getAverageThroughput(),  database.getClass().getSimpleName() + " OLTP + OLAP", "OLTP Throughput Comparison");
            OLAPdataset.addValue(htapResponseTimeStatistics.getAverageResponseTime(),  database.getClass().getSimpleName() + " OLTP + OLAP", "OLAP Response Time Comparison");
            OLTPdataset.addValue(oltpThroughputStatistic.getAverageThroughput(),  database.getClass().getSimpleName() + " OLTP", "OLTP Throughput Comparison");
            OLAPdataset.addValue(olapResponseTimeStatistics.getAverageResponseTime(),  database.getClass().getSimpleName() + " OLAP", "OLAP Response Time Comparison");

            throughputDataset.addSeries(throughputSingleSeries);
            throughputDataset.addSeries(throughputParallelSeries);
            responseTimeDataset.addSeries(responseTimeSingleSeries);
            responseTimeDataset.addSeries(responseTimeParallelSeries);
            database.disconnect();
        }
        executorService.shutdown();

        Charter.createXYLineChart("OLTP Throughput Comparison", "Run", "Throughput (operations per second)", throughputDataset);
        Charter.createXYLineChart("OLAP Response Time Comparison", "Run", "Response Time (milliseconds)", responseTimeDataset);

        Charter.createBarChart("OLTP Performance", "Run", "Throughput (operations per second)", OLTPdataset);
        Charter.createBarChart("OLAP Performance", "Run", "Response Time (milliseconds)", OLAPdataset);
    }
}
