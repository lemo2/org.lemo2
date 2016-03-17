package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.lemo2.dataprovider.api.DataProvider;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.jdbc.JDBC_Context;
import org.lemo2.dataprovider.jdbc.JDBC_DataProvider;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

public class MyTest {

	private Map<String, Long> durations = new HashMap<String, Long>();
	
	@Test
	public void testContextPrint() {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		long start = 0;
		
		try {
			DataProvider dp = new MongoDB_DataProvider(host, database, port);
			
			System.out.println("Test-Start: ");
			start = System.currentTimeMillis();
			System.out.println("Timestamp-Start: " + start);
			
			for ( LA_Context context : dp.getCourses() ) {
				MongoDB_Context mContext = (MongoDB_Context) context;
				if (mContext.getID() == 206) {
					printContext(mContext, "");
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("Test-End: ");
			System.out.println("Timestamp-End: " + end);
			long duration = end - start;
			long secDuration = TimeUnit.MILLISECONDS.toSeconds(duration);
			System.out.println("Duration for Test: " + secDuration + " seconds");
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
		// print duration map
		printMap();
		printInitializedCounts();
	}
	
	private void printContext(MongoDB_Context context, String prefix) {
		
		List<LA_Person> instructors = new ArrayList<LA_Person>();
		List<LA_Person> students = new ArrayList<LA_Person>();
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<LA_Object> lObjects = new ArrayList<LA_Object>();
		
		long start = 0;
		long end = 0;
		long duration = 0;
		long secDuration = 0;
		
		System.out.print(prefix + "CONTEXT: " + context.getName());
		Set<String> attributes = context.extAttributes();
		if ( attributes != null ) {
			for ( String attr : attributes ) {
				System.out.print(" " + attr + ": " + context.getExtAttribute(attr));
			}
		}
		System.out.println();
		prefix += "    ";
		
		// get instructors
		start = System.currentTimeMillis();
		instructors = context.getInstructors(); // instructors
		end = System.currentTimeMillis();
		duration = end - start;
		secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("TIMING initialize instructors: " + secDuration + " ms");
		addToMap("Instructor", secDuration);
		printFirstInstructor(instructors, prefix);
		
		// get students
		start = System.currentTimeMillis();
		students = context.getStudents(); // students
		end = System.currentTimeMillis();
		duration = end - start;
		secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("TIMING initialize students: " + secDuration + " ms");
		addToMap("Student", secDuration);
		printFirstStudent(students, prefix);
		
		// get learning objects
		start = System.currentTimeMillis();
		lObjects = context.getObjects(); // students
		end = System.currentTimeMillis();
		duration = end - start;
		secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("TIMING initialize learning objects: " + secDuration + " ms");
		addToMap("LObject", secDuration);
		printFirstObject(lObjects, prefix);
		
		// get learning activitiy
		start = System.currentTimeMillis();
		activities = context.getActivities(); // students
		end = System.currentTimeMillis();
		duration = end - start;
		secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("TIMING initialize activities: " + secDuration + " ms");
		addToMap("Activity", secDuration);
		printFirstActivity(activities, prefix);
		
		// get children
		List<LA_Context> children = context.getChildren();
		if ( children == null ) return;
		for ( LA_Context child : children ) {
			printContext((MongoDB_Context) child, prefix);
		}
	}
	
	private void printFirstInstructor(List<LA_Person> instructors, String prefix) {
		for ( LA_Person person : instructors ) {
			System.out.print(prefix + "FIRST INSTRUCTOR (1/" + instructors.size() + "):  name: ");
			System.out.print(person.getName());
			Set<String> attributes = person.extAttributes();
			if ( attributes != null ) {
				for ( String attr : attributes ) {
					System.out.print("  " + attr + ": " + person.getExtAttribute(attr));
				}
			}
			System.out.println();
			return;
		}
	}
	
	private void printFirstStudent(List<LA_Person> students, String prefix) {
		for ( LA_Person person : students ) {
			System.out.print(prefix + "FIRST STUDENT (1/" + students.size() + "):  name: ");
			System.out.print(person.getName());
			Set<String> attributes = person.extAttributes();
			if ( attributes != null ) {
				for ( String attr : attributes ) {
					System.out.print("  " + attr + ": " + person.getExtAttribute(attr));
				}
			}
			System.out.println();
			return;
		}
	}
	
	private void printFirstObject(List<LA_Object> objects, String prefix) {
		for ( LA_Object object : objects ) {
			System.out.print(prefix + "FIRST OBJECT (1/" + objects.size() + "):  name: ");
			System.out.print(object.getName() + "  type: " + object.getType());
			Set<String> attributes = object.extAttributes();
			if ( attributes != null ) {
				for ( String attr : attributes ) {
					System.out.print("  " + attr + ": " + object.getExtAttribute(attr));
				}
			}
			System.out.println();
			if ( object.getChildren() == null ) return;
			printFirstObject(object.getChildren(), prefix + "    ");
			return;
		}
	}
		
	private void printFirstActivity(List<LA_Activity> activities, String prefix) {
		for ( LA_Activity activity : activities ) {
			System.out.print(prefix + "FIRST ACTIVITY (1/" + activities.size() + "):  time: ");
			System.out.print(activity.getTime());
			Set<String> attributes = activity.extAttributes();
			if ( attributes != null ) {
				for ( String attr : activity.extAttributes() ) {
					System.out.print("  " + attr + ": " + activity.getExtAttribute(attr));
				}
			}
			System.out.println();
			return;
		}
	}
	
	private void addToMap(String key, Long valueToAdd) {
		Long oldValue = durations.get(key);
		if (oldValue != null) {
			Long newValue = oldValue + valueToAdd;
			durations.put(key, newValue);
		}
		else {
			durations.put(key, valueToAdd);
		}
	}

	private void printMap() {
	    Iterator it = durations.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String) pair.getKey();
	        Long value = (Long) pair.getValue();
	        
	        System.out.println(pair.getKey() + " : " + pair.getValue() + " ms");
	        // ms to s
	        value = TimeUnit.MILLISECONDS.toSeconds(value);
	        System.out.println(pair.getKey() + " : " + value + " seconds");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	private void printInitializedCounts() {
		int sizeActivities = MongoDB_ActivityDataProvider.getSizeOfInitializedActivities();
		int sizeContexts = MongoDB_ContextDataProvider.getSizeOfInitializedContexts();
		int sizeObjects = MongoDB_ObjectDataProvider.getSizeOfInitializedLearningObjects();
		int sizePersons = MongoDB_PersonDataProvider.getSizeOfInitializedPersons();
		
		System.out.println("Size contexts: " + sizeContexts);
		System.out.println("Size activities: " + sizeActivities);
		System.out.println("Size objects: " + sizeObjects);
		System.out.println("Size persons: " + sizePersons);
	}
}
