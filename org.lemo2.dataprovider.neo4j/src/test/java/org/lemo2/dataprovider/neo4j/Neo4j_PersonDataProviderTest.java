package org.lemo2.dataprovider.neo4j;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;

public class Neo4j_PersonDataProviderTest {

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
