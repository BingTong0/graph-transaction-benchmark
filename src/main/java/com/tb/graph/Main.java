package com.tb.graph;

import com.tb.graph.database.GalaxybaseBenchmark;
import com.tb.graph.database.Neo4jBenchmark;

public class Main {

    public static void main(String[] args) {
        GraphDatabaseBenchmark neo4jBenchmark = new Neo4jBenchmark();
        GraphDatabaseBenchmark galaxybaseBenchmark = new GalaxybaseBenchmark();
        Driver.compareDatabases(neo4jBenchmark, galaxybaseBenchmark);
    }
}
