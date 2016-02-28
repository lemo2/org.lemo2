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

public class MongoDB_ActivityTest {

		private static MongoDB_DataProvider dataProvider;
		
		private int contextID = 205;
		private int objectID = 121076;
		private int personID = 405083;
		
		@BeforeClass
		public static void setUp() throws Exception {
			String database = "iversity";
			String host = "localhost";
			int port = 27017;
			
			System.out.println("Create MongoDB-Conntection");
			dataProvider = new MongoDB_DataProvider(host, database, port);
		}
		
		@Test
		public void getAllActivityIDsTest() {
			List<Integer> activities = new ArrayList<Integer>();
			
			long start = System.currentTimeMillis();
			activities = MongoDB_ActivityDataProvider.getAllLearningActivityIDs();
			
			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Activity-IDs size: " + activities.size());
			System.out.println("Seconds - get all activity IDs: " + secDuration + " ms");
		}
		
		@Test
		public void getContextActivities() {
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
			
			activities = context.getActivities();

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size - activities of context: " + activities.size());
			System.out.println("Seconds - get activities of context '" + 
					context.getName() + "' : " + secDuration + " ms");
		}
		
		@Test
		public void getContextActivitiesRecursive() {
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
			
			activities = context.getActivitiesRecursive();

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size - recursive activities of context: " + activities.size());
			System.out.println("Seconds - get recursive activities of context '" + 
					context.getName() + "' : " + secDuration + " ms");
		}
		
		@Test
		public void getActivitiesByObject_WildcardPersonAndTimerange() {
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
			
			activities = MongoDB_ActivityDataProvider.getActivities(lObject);

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size of activities by learning objects: " + activities.size());
			System.out.println("Seconds - get activities for learning objects '" + 
					lObject.getName() + "' : " + secDuration + " ms");
		}
		
		@Test
		public void getActivitiesByObject_WildcardPerson() {
			Integer contextID = 212681;
			long activityStart = 1414800000; // 30.11.2014 00:00:00
			long activityEnd = 1417443573; // 01.12.2014 14:19:33
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
			LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
			
			activities = context.getActivities(null, lObject, activityStart, activityEnd);

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size of activities by learning object and timerange: " + activities.size());
			System.out.println("Seconds - get activities for learning object and timerange '" + 
					lObject.getName() + "' : " + secDuration + " ms");
		}
		
		@Test
		public void getActivitiesByPerson_WildcardLearningObject() {
			Integer contextID = 212681;
			long activityStart = 1414800000; // 30.11.2014 00:00:00
			long activityEnd = 1417443573; // 01.12.2014 14:19:33
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
			LA_Person person = MongoDB_PersonDataProvider.getPersonByID(personID);
			
			activities = context.getActivities(person, null, activityStart, activityEnd);

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size of activities by person and timerange: " + activities.size());
			System.out.println("Seconds - get activities for person and timerange '" + 
					person.getName() + "' : " + secDuration + " ms");
		}
				
		@Test
		public void getActivitiesByObject_WildcardPersonAndLearningObject() {
			Integer contextID = 212681;
			long activityStart = 1414800000; // 01.11.2014 00:00:00
			long activityEnd = 1417443573; // 01.12.2014 14:19:33
			List<LA_Activity> activities = new ArrayList<LA_Activity>();
			
			long start = System.currentTimeMillis();
			
			LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
			
			activities = context.getActivities(null, null, activityStart, activityEnd);

			long end = System.currentTimeMillis();
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
			System.out.println("Size of activities by timerange: " + activities.size());
			System.out.println("Seconds - get activities for timerange: " + secDuration + " ms");
		}
}
