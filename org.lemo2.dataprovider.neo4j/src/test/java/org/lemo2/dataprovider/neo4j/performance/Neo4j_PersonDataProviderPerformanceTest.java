package org.lemo2.dataprovider.neo4j.performance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.neo4j.Neo4j_Connector;
import org.lemo2.dataprovider.neo4j.Neo4j_PersonDataProvider;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4j_PersonDataProviderPerformanceTest {

	@Before
	public void setUp() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "!!12L3m0" ) );
		Neo4j_Connector.setDriver(driver);
	}
	
	@Test
	public void getActivitiesOfPerson() {
		String personID = "4074";
		List<LA_Activity> activities = Neo4j_PersonDataProvider.getActivitiesOfPerson(personID);
		
		assertEquals(17210, activities.size());
	}
	
	@Test
	public void getContextsOfPerson() {
		String personID = "1";
		List<LA_Context> contexts = Neo4j_PersonDataProvider.getContextsOfPerson(personID);
		
		assertEquals(3, contexts.size());
	}

}
