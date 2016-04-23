package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Person;

public class MongoDB_PersonTest {
	
	private int personID = 21797; // has 17423 activities
	
	@BeforeClass
	public static void setUp() throws Exception {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		System.out.println("Create MongoDB-Conntection to person collection");
		MongoDB_DataProvider dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	private void clearData() {
		MongoDB_ActivityDataProvider.clearInitializedActivities();
		MongoDB_ContextDataProvider.clearInitializedContexts();
		MongoDB_PersonDataProvider.clearInitializedPersons();
		MongoDB_ObjectDataProvider.clearInitializedLearningObjects();
	}
	
	//@Test
	public void getPersonActivitiesTest() {
		clearData();
		
		LA_Person person = MongoDB_PersonDataProvider.getPersonByID(personID);
		
		assertNotNull(person);
		
		long start = System.currentTimeMillis();
		List<LA_Activity> activities = MongoDB_PersonDataProvider.getPersonActivities(personID);
		
		assertNotNull(activities);
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count activities of person '" + person.getName() + "' - " + activities.size()
				+ " activities");
		System.out.println("Seconds - get person activities: " + secDuration + " ms");
	}
	
	@Test
	public void getAllPersonsTest() {
		clearData();
		
		List<Integer> personIDs = MongoDB_PersonDataProvider.getAllPersonIDs();
		
		long start = System.currentTimeMillis();
		List<LA_Person> persons = MongoDB_PersonDataProvider.getPersonsByIDList(personIDs);
		
		assertNotNull(persons);
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count persons: " + persons.size());
		System.out.println("Seconds - get persons by ID list: " + secDuration + " ms");
	}

}
