package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
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
	private static final int STORYTELLING_CONTEXT_ID = 206;
	private static final int PYTHAGORAS_CONTEXT_ID = 21210;
	
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

		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
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
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
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
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
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

		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
		assertNotNull(context);
		List<LA_Context> childrenTree = new ArrayList<LA_Context>();
		childrenTree = MongoDB_ContextDataProvider.getFirstDegreeChildrenOfContext(STORYTELLING_CONTEXT_ID);
		
		assertNotNull(childrenTree);
		
		System.out.println("-----------------------------------------");
		System.out.println("Count first degree children of context '" + context.getName() + 
				"' - " + childrenTree.size());
		assertEquals(17, childrenTree.size());
	}
	
	//@Test
	public void countContextActivitiesTest() {
		clearData();
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
		assertNotNull(context);
		
		int numberOfActivities = MongoDB_ContextDataProvider.countContextActivities(context.getID());
		
		System.out.println("-----------------------------------------");
		System.out.println("Number of activities for context '" + context.getName() + "' : " + numberOfActivities);
	}
	
	//@Test
	public void countContextActivitiesTest2() {
		clearData();

		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID); 
		
		assertNotNull(context);
		
		int numberOfActivities = MongoDB_ActivityDataProvider.countActivitiesOfContext(context.getID());
		
		System.out.println("-----------------------------------------");
		System.out.println("Number of activities for context '" + context.getName() + "' : " + numberOfActivities);
	}
	
	@Test
	public void getAllCoursesTest() {
		clearData();
		
		long start = System.currentTimeMillis();
		
		Set<LA_Context> courses = MongoDB_ContextDataProvider.getAllCourses();
		
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
	public void initializeContextPerformanceTest() {
		clearData();
		
		long start = System.currentTimeMillis();

		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
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
		
		long start = System.currentTimeMillis();
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(STORYTELLING_CONTEXT_ID);
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
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(PYTHAGORAS_CONTEXT_ID);
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
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(PYTHAGORAS_CONTEXT_ID);
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
		Set<LA_Context> courses = dataProvider.getCourses();
		long stopTime = System.currentTimeMillis();
		
		Assert.assertNotNull(courses);
		System.out.println(courses.size());
		System.out.println((stopTime - startTime) / 1000);
	}

}
