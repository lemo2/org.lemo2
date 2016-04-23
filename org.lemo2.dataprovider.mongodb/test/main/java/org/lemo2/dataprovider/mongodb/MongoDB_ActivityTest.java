package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

public class MongoDB_ActivityTest {

	private static MongoDB_DataProvider dataProvider;
	
	private int STORYTELLING_CONTEXT_ID = 206;
	private int objectID = 142423;
	private int personID = 287630;
	
	@BeforeClass
	public static void setUp() throws Exception {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		System.out.println("Create MongoDB-Conntection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	private void clearData() {
		MongoDB_ActivityDataProvider.clearInitializedActivities();
		MongoDB_ContextDataProvider.clearInitializedContexts();
		MongoDB_PersonDataProvider.clearInitializedPersons();
		MongoDB_ObjectDataProvider.clearInitializedLearningObjects();
	}
	
	//@Test
	public void getAllActivityIDsTest() {
		clearData();
		
		List<Integer> activities = new ArrayList<Integer>();
		
		long start = System.currentTimeMillis();
		activities = MongoDB_ActivityDataProvider.getAllLearningActivityIDs();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Activity-IDs size: " + activities.size());
		System.out.println("Seconds - get all activity IDs: " + secDuration + " ms");
	}
	
	//@Test
	public void getContextActivities() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(212741);
		
		long start = System.currentTimeMillis();
		
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
	
	//@Test
	public void getContextActivities2() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(212741);
		
		long start = System.currentTimeMillis();
		
		activities = MongoDB_ActivityDataProviderWithConId.getActivities(context);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size - activities2 of context: " + activities.size());
		System.out.println("Seconds - get activities2 of context '" + 
				context.getName() + "' : " + secDuration + " ms");
	}
	
	//@Test
	public void getContextActivitiesRecursive() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
		
		long start = System.currentTimeMillis();
		
		activities = context.getAllActivities();

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
	public void getContextActivities2Recursive() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
		MongoDB_Context mContext = (MongoDB_Context) context;
		
		long start = System.currentTimeMillis();
		
		activities = mContext.getAllActivities2();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context - '" + context.getName() + 
				"': Size - recursive activities of context: " + activities.size());
		System.out.println("Seconds - get recursive activities of context '" + 
				context.getName() + "' : " + secDuration + " ms");
	}
	
	//@Test
	public void getActivitiesByObject_WildcardPersonAndTimerange() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
		
		activities = MongoDB_ActivityDataProvider.getActivities(lObject);

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Learning object - '" + lObject.getName() + 
				"': Size of activities by learning objects: " + activities.size());
		System.out.println("Seconds - get activities for learning objects '" + 
				lObject.getName() + "' : " + secDuration + " ms");
	}
	
	//@Test
	public void getActivitiesByObject_WildcardPerson() {
		clearData();
	
		long activityStart = 1414800000; // 30.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
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
	
	//@Test
	public void getActivitiesByPerson_WildcardLearningObject() {
		clearData();

		long activityStart = 1414800000; // 30.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
		LA_Person person = MongoDB_PersonDataProvider.getPersonByID(personID);
		
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
			
	//@Test
	public void getActivitiesByObject_WildcardPersonAndLearningObject() {
		clearData();
	
		long activityStart = 1414800000; // 01.11.2014 00:00:00
		long activityEnd = 1417443573; // 01.12.2014 14:19:33
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		long start = System.currentTimeMillis();
		
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
		
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
