package com.tb.graph.database;

import java.util.Map;


import com.graphdbapi.driver.Graph;
import com.graphdbapi.driver.GraphDb;
import com.graphdbapi.driver.v1.Driver;
import com.graphdbapi.driver.v1.Values;
import com.tb.graph.GraphDatabaseBenchmark;

/**
 * Galaxybase Graph database
 * https://www.galaxybase.com/
 */
public class GalaxybaseBenchmark extends GraphDatabaseBenchmark {

    private Driver driver;
    private Graph graph;

    @Override
    public void connect() {
        this.driver = GraphDb.connect("bolt://192.168.40.130:7687", "admin", "admin");
        this.graph = GraphDb.driver(this.driver, "1400w1");
    }

    @Override
    public void disconnect() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    @Override
    public void addNode(String nodeId) {
        graph.executeCypher("CREATE (n:person {id: '$nodeId'})", Map.of("nodeId", Values.value(nodeId)));
    }

    @Override
    public void deleteNode(String nodeId) {
        graph.executeCypher("MATCH (n:person {id: '$nodeId'}) DELETE n", Map.of("nodeId", Values.value(nodeId)));
    }

    @Override
    public void getNode(String nodeId) {
        graph.executeCypher("MATCH (n:person {id: '$nodeId'}) RETURN n", Map.of("nodeId", Values.value(nodeId)));
    }

    @Override
    public void executeOLAPAnalysis() {
        graph.executeQuery("CALL gapl.PageRank({maxIterations:20, dampingFactor:0.85, order:false, primaryKey:false, initValue:1.0, limit:-1, vertexTypes:[], edgeTypes:[], memory:true, vertexIteratorLevel:0})");
        System.out.println("PageRank algorithm executed successfully.");
    }

}
