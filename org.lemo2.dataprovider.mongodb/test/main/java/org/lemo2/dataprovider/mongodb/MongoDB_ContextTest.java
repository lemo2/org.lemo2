package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

import com.mongodb.DBObject;

public class MongoDB_ContextTest {

	private static MongoDB_DataProvider dataProvider;
	
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
	public void getChildrenTreeActivitiesTest() {
		clearData();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		int contextID = 206;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		List<LA_Context> childrenTree = new ArrayList<LA_Context>();
		childrenTree = MongoDB_ContextDataProvider.getChildrenTreeOfContext(context, childrenTree);
		
		activities.addAll(context.getActivities());
		for (LA_Context child : childrenTree) {
			activities.addAll(child.getActivities());
		}
		
		assertNotNull(childrenTree);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count children of context '" + context.getName() + "' - " + childrenTree.size()
				+ " children");
		System.out.println("Count activities of children '" + context.getName() + "' - " + activities.size() 
				+ " activities");
	}
	
	//@Test
	public void getChildrenTreeActivities_noInitializationTest() {
		clearData();
		
		List<DBObject> activities = new ArrayList<DBObject>();
		
		int contextID = 206;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		List<LA_Context> childrenTree = new ArrayList<LA_Context>();
		childrenTree = MongoDB_ContextDataProvider.getChildrenTreeOfContext(context, childrenTree);
		
		activities.addAll(MongoDB_ActivityDataProvider.getActivities_Test(context));
		for (LA_Context child : childrenTree) {
			activities.addAll(MongoDB_ActivityDataProvider.getActivities_Test(context));
		}
		
		assertNotNull(childrenTree);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count children of context '" + context.getName() + "' - " + childrenTree.size()
				+ " children");
		System.out.println("Count activities of children '" + context.getName() + "' - " + activities.size() 
				+ " activities");
	}
	
	//@Test
	public void getChildrenTreeTest() {
		clearData();
		
		int contextID = 205;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		List<LA_Context> childrenTree = new ArrayList<LA_Context>();
		childrenTree = MongoDB_ContextDataProvider.getChildrenTreeOfContext(context, childrenTree);
		
		assertNotNull(childrenTree);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count children of context '" + context.getName() + "' - " + childrenTree.size());
	}
	
	//@Test
	public void getFirstDegreeChildrenTest() {
		clearData();
		
		int contextID = 205;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		List<LA_Context> childrenTree = new ArrayList<LA_Context>();
		childrenTree = MongoDB_ContextDataProvider.getFirstDegreeChildrenOfContext(contextID);
		
		assertNotNull(childrenTree);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count first degree children of context '" + context.getName() + 
				"' - " + childrenTree.size());
		assertEquals(17, childrenTree.size());
	}
	
	@Test
	public void countContextActivitiesTest() {
		clearData();
		
		int contextID = 205;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		
		int numberOfActivities = MongoDB_ContextDataProvider.countContextActivities(context.getID());
		
		System.out.println("-----------------------------------------");
		System.out.println("Number of activities for context '" + context.getName() + "' : " + numberOfActivities);
	}
	
	@Test
	public void countContextActivitiesTest2() {
		clearData();
		
		int contextID = 205;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID); 
		
		assertNotNull(context);
		
		int numberOfActivities = MongoDB_ActivityDataProvider.countActivitiesOfContext(context.getID());
		
		System.out.println("-----------------------------------------");
		System.out.println("Number of activities for context '" + context.getName() + "' : " + numberOfActivities);
	}
	
	//@Test
	public void getAllCoursesTest() {
		clearData();
		
		long start = System.currentTimeMillis();
		
		List<LA_Context> courses = MongoDB_ContextDataProvider.getAllCourses();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("All Courses size " + courses.size());
		System.out.println("Duration - get all courses: " + secDuration + " ms");
		
		System.out.println("Get size of initialized contexts: " + 
				MongoDB_ContextDataProvider.getSizeOfInitializedContexts());
	}
	
	//@Test
	public void getAllCourses2Test() {
		clearData();
		
		long start = System.currentTimeMillis();
		
		List<LA_Context> courses = MongoDB_ContextDataProvider.getAllCourses2();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("All Courses size " + courses.size());
		System.out.println("Duration - get all courses2: " + secDuration + " ms");
		
		System.out.println("Get size of initialized contexts: " + 
				MongoDB_ContextDataProvider.getSizeOfInitializedContexts());
	}
	
	////@Test
	public void initializeContextPerformanceTest() {
		clearData();
		
		long start = System.currentTimeMillis();
		Integer contextID = 205;//212741; // 566475 activities
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		List<LA_Activity> activities = context.getActivities();
		
		List<LA_Context> children = context.getChildren();
		
		System.out.println("Children size: " + children.size());
		for (LA_Context child : children) {
			System.out.println("Child: " + child.getName());
			activities.addAll(child.getActivities());
		}
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Initialize Context '" + context.getName() + "' activities: " + activities.size());
		System.out.println("Duration - initialize context '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getLearningObjects() {
		clearData();
		
		int contextID = 205;
		
		long start = System.currentTimeMillis();
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		List<LA_Object> lObjects = context.getObjects();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context learning objects size " + lObjects.size());
		System.out.println("Duration - get context learning objects: " + secDuration + " ms");
	}
	
	//@Test
	public void getStudentsOfCourseTest() {
		clearData();
		
		int contextID = 21210;
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		long start = System.currentTimeMillis();
		List<LA_Person> students = context.getStudents();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context '" + context.getName() + "' size students: " + students.size());
		System.out.println("Duration - get students for course '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getInstructorsOfCourseTest() {
		clearData();
		
		int contextID = 21210;
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		long start = System.currentTimeMillis();
		
		List<LA_Person> instructors = context.getInstructors();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		
		System.out.println("-----------------------------------------");
		System.out.println("Context '" + context.getName() + "' size instructors: " + instructors.size());
		System.out.println("Duration - get instructors for course '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getCoursesPerformanceTest() {
		clearData();
		
		long startTime = System.currentTimeMillis();
		List<LA_Context> courses = dataProvider.getCourses();
		long stopTime = System.currentTimeMillis();
		
		Assert.assertNotNull(courses);
		System.out.println(courses.size());
		System.out.println((stopTime - startTime) / 1000);
	}

}
