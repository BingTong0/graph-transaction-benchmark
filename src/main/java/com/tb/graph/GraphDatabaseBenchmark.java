package com.tb.graph;

/**
 * benchmark case
 * every graph database will run the same case
 */
public abstract class GraphDatabaseBenchmark {
    // connect database
    public abstract void connect();

    // close
    public abstract void disconnect();

    // OLTP addNode
    public abstract void addNode(String nodeId);

    // OLTP deleteNode
    public abstract void deleteNode(String nodeId);

    // OLTP getNode
    public abstract void getNode(String nodeId);

    // OLAP run algorithm
    public abstract void executeOLAPAnalysis();

    public double measureOLTPThroughput() {
        long startTime = System.currentTimeMillis();

        // oltp execution 2s
        int count = 0;
        while (System.currentTimeMillis() - startTime <= 2000) {
            deleteNode("" + count);
            addNode("" + count);
            getNode("" + count);
            count++;
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        double throughput = 1000.0 * count / elapsedTime ;
        System.out.println("OLTP Count: " + count + " Time: " +elapsedTime + " milliseconds Throughput: " + throughput + " operations per second");
        return throughput;
    }

    public long measureOLAPResponseTime() {
        long startTime = System.currentTimeMillis();

        executeOLAPAnalysis();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("OLAP Response Time: " + elapsedTime + " milliseconds");
        return elapsedTime;
    }
}
