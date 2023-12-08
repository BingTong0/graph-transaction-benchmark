package com.tb.graph.database;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Values;

import com.tb.graph.GraphDatabaseBenchmark;

/**
 * Neo4j Graph Database
 * https://neo4j.com/
 */
public class Neo4jBenchmark extends GraphDatabaseBenchmark {

    private Driver driver;

    @Override
    public void connect() {
        Config config = Config.builder().build();
        this.driver = GraphDatabase.driver("bolt://192.168.40.127:7687", AuthTokens.basic("neo4j", "123456"), config);
    }

    @Override
    public void disconnect() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    @Override
    public void addNode(String nodeId) {
        driver.session().run("CREATE (n:person {id: '$nodeId'})", Values.parameters("nodeId", nodeId));
    }

    @Override
    public void deleteNode(String nodeId) {
        driver.session().run("MATCH (n:person {id: '$nodeId'}) DELETE n", Values.parameters("nodeId", nodeId));
    }

    @Override
    public void getNode(String nodeId) {
        driver.session().run("MATCH (n:person {id: '$nodeId'}) RETURN n", Values.parameters("nodeId", nodeId));
    }

    @Override
    public void executeOLAPAnalysis() {
        // CALL gds.graph.create('myGraph', 'person', 'know')
        String query = "CALL gds.pageRank.stream('myGraph', {maxIterations: 20, dampingFactor: 0.85 })";
        driver.session().run(query);
        System.out.println("PageRank algorithm executed successfully.");
    }
}
