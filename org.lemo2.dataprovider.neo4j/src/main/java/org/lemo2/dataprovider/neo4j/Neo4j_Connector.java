package org.lemo2.dataprovider.neo4j;

import java.io.File;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4j_Connector {

	private static File DB_PATH;
	private GraphDatabaseService graphDB;
	
	public Neo4j_Connector(String host, String databaseName, int databasePort) {
		
	}
	
	public Neo4j_Connector(String host, String databaseName, int databasePort, String configPath) {
		Node node;
	}
	
	private void connectToDatabase(String databasePath) throws Exception {
		DB_PATH = new File(databasePath);
		graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	public static Driver getDriver() {
		
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "!!12L3m0" ) );
		
		return driver;
	}

}
