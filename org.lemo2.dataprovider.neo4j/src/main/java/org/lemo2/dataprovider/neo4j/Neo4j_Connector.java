package org.lemo2.dataprovider.neo4j;

import java.io.File;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;

public class Neo4j_Connector {

	private static File DB_PATH;
	private static GraphDatabaseService graphDB;
	
	private static Driver driver;
	
	public static void createImpermanentDatabase() {
		graphDB = new TestGraphDatabaseFactory().newImpermanentDatabase();
		registerShutdownHook(graphDB);
	}
	
	public static GraphDatabaseService getGraphdatabase() {
		return graphDB;
	}
	
	public static GraphDatabaseService createEmbeddedDatabase(String databasePath) {
		
		if (graphDB == null) {
			GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
			DB_PATH = new File(databasePath);
			graphDB = dbFactory.newEmbeddedDatabase(DB_PATH);
			registerShutdownHook(graphDB);
		}
		
		return graphDB;
	}
	
	public static void shutdownEmbeddedDatabase() {
		graphDB.shutdown();
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
		if (driver == null) {
			return getDefaultDriver();
		}
		return driver;
	}
	
	private static Driver getDefaultDriver() {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "neo4j" ) );
		return driver;
	}
	
	public static void setDriver(String boltURL, AuthToken authToken) {
		driver = GraphDatabase.driver(boltURL, authToken);
	}
	
	public static void setDriver(String boltURL, Config config) {
		driver = GraphDatabase.driver(boltURL, config);
	}
	
	public static void setDriver(Driver drv) {
		driver = drv;
	}

}
