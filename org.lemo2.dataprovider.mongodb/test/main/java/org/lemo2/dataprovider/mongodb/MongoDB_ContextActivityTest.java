package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;

public class MongoDB_ContextActivityTest {
	
	private int contextID = 206;
	private int objectID = 142423;
	private int personID = 287630;
	
	@BeforeClass
	public static void setUp() throws Exception {
		MongoDB_DataProvider dataProvider;
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		System.out.println("Create MongoDB-Conntection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	private void clearData() {
		MongoDB_ActivityDataProvider2.clearInitializedActivities();
		MongoDB_ContextDataProvider2.clearInitializedContexts();
		MongoDB_PersonDataProvider.clearInitializedPersons();
		MongoDB_ObjectDataProvider.clearInitializedLearningObjects();
	}
	
	@Test
	public void getActivities_NewProviderTest() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		
		assertNotNull(context);
		activities = context.getActivities();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Size - (new provider) get activities of context: " + activities.size());
		System.out.println("Seconds - (new provider) get activities of context '" + 
				context.getName() + "' : " + secDuration + " ms");
		
	}
	
	@Test
	public void getActivities_OriginalProviderTest() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		
		assertNotNull(context);
		activities = context.getActivities();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Size - (original provider) get activities of context: " + activities.size());
		System.out.println("Seconds - (original provider) get activities of context '" + 
				context.getName() + "' : " + secDuration + " ms");
		
	}
	
	@Test
	public void getContextActivities() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		
		activities = context.getActivities();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size - activities of context: " + activities.size());
		System.out.println("Seconds - get activities of context '" + 
				context.getName() + "' : " + secDuration + " ms");
	}
	
	@Test
	public void getContextActivitiesRecursive() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		
		activities = context.getActivitiesRecursive();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size - recursive activities of context: " + activities.size());
		System.out.println("Seconds - get recursive activities of context '" + 
				context.getName() + "' : " + secDuration + " ms");
	}
	
	@Test
	public void getActivitiesByObject_WildcardPersonAndTimerange() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
		
		activities = context.getActivities(null, lObject);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Learning object - '" + lObject.getName() + 
				"': Size of activities by learning objects: " + activities.size());
		System.out.println("Seconds - get activities for learning objects '" + 
				lObject.getName() + "' : " + secDuration + " ms");
	}
	
	@Test
	public void getActivitiesByObject_WildcardPerson() {
		clearData();
		
		//Integer contextID = 212681;
		long activityStart = 1414800000; // 30.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
		
		activities = context.getActivities(null, lObject, activityStart, activityEnd);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size of activities by learning object and timerange: " + activities.size());
		System.out.println("Seconds - get activities for learning object and timerange '" + 
				lObject.getName() + "' : " + secDuration + " ms");
	}
	
	@Test
	public void getActivitiesByPerson_WildcardLearningObject() {
		clearData();
		
		//Integer contextID = 212681;
		long activityStart = 1414800000; // 30.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		LA_Person person = MongoDB_PersonDataProvider.getPersonByID(personID);
		
		assertNotNull(context);
		assertNotNull(person);
		
		activities = context.getActivities(person, null, activityStart, activityEnd);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size of activities by person and timerange: " + activities.size());
		System.out.println("Seconds - get activities for person and timerange '" + 
				person.getName() + "' : " + secDuration + " ms");
	}
			
	@Test
	public void getActivitiesByObject_WildcardPersonAndLearningObject() {
		clearData();
		//Integer contextID = 212681;
		long activityStart = 1414800000; // 01.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider2.getContextByID(contextID);
		
		activities = context.getActivities(null, null, activityStart, activityEnd);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size of activities by timerange: " + activities.size());
		System.out.println("Seconds - get activities for timerange: " + secDuration + " ms");
	}

}
