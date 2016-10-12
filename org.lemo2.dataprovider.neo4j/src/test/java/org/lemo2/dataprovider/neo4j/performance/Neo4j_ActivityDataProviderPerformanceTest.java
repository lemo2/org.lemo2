package org.lemo2.dataprovider.neo4j.performance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.Neo4j_ActivityDataProvider;
import org.lemo2.dataprovider.neo4j.Neo4j_Connector;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Activity;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Object;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Person;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4j_ActivityDataProviderPerformanceTest {

	LA_Context context;
	LA_Person person;
	LA_Object obj;
	
	@Before
	public void setUp() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "!!12L3m0" ) );
		Neo4j_Connector.setDriver(driver);
	}
	
	@Test
	public void getActionOfActivity() {
		String activityID = "1";
		
		String action = Neo4j_ActivityDataProvider.getActionOfActivity(activityID);
		
		assertEquals("Record_progress", action);
	}
	
	@Test
	public void getReferenceOfActivity() {
		LA_Activity activity = new Neo4j_Activity("140773");
		LA_Activity reference;
		
		reference = activity.getReference();
		Neo4j_Activity nReference = (Neo4j_Activity) reference;
		
		assertEquals("123024", nReference.getActivityID());
	}
	
	@Test
	public void getLearningObjectOfActivity() {
		LA_Activity activity = new Neo4j_Activity("140773");
		LA_Object lObject;
		
		lObject = activity.getObject();
		Neo4j_Object nObject = (Neo4j_Object) lObject;
		
		assertEquals("106920", nObject.getObjectID());
	}
	
	@Test
	public void getPersonOfActivity() {
		LA_Activity activity = new Neo4j_Activity("140773");
		LA_Person person;
		
		person = activity.getPerson();
		Neo4j_Person nPerson = (Neo4j_Person) person;
		
		assertEquals("101510", nPerson.getPersonID());
	}
	
	@Test
	public void getActivitiesOfContext_Parameters_Context() {
		context = new Neo4j_Context("182");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getActivitiesOfContext(context);
		
		assertEquals(397781, activities.size());
	}

	@Test
	public void getActivitiesOfContext_Parameters_ContextAndPerson() {
		context = new Neo4j_Context("74");
		person = new Neo4j_Person("2740");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getActivitiesOfContext(context, person);
		
		assertEquals(1848, activities.size());
	}
	
	@Test
	public void getActivitiesOfContext_Parameters_ContextAndPersonAndObject() {
		context = new Neo4j_Context("74");
		person = new Neo4j_Person("2740");
		obj = new Neo4j_Object("107744");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getActivitiesOfContext(context, person, obj);
		
		assertEquals(1848, activities.size());
	}

	@Test
	public void getActivitiesOfContext_Parameters_ContextAndPersonAndNullObject() {
		context = new Neo4j_Context("74");
		person = new Neo4j_Person("2740");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getActivitiesOfContext(context, person, null);
		
		assertEquals(1848, activities.size());
	}
	
	@Test
	public void getAllActivitiesOfContext_Parameters_ContextAndPersonAndLearningObject() {
		context = new Neo4j_Context("74");
		person = new Neo4j_Person("2740");
		obj = new Neo4j_Object("107744");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getAllActivitiesOfContext(context, person, obj);
		
		assertEquals(1848, activities.size());
	}
	
	//@Test
	public void getAllActivitiesOfContext_Parameters_Context() {
		context = new Neo4j_Context("2");
		
		List<LA_Activity> activities = Neo4j_ActivityDataProvider.getAllActivitiesOfContext(context);
		
		assertEquals(5060767, activities.size());
	}
}
