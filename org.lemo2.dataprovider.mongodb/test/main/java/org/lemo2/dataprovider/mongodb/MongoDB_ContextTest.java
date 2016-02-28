package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

public class MongoDB_ContextTest {

	private MongoDB_DataProvider dataProvider;
	
	private String database = "iversity";
	private String host = "localhost";
	private int port = 27017;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Create MongoDB-Conntection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	//@Test
	public void getAllCoursesTest() {
		long start = System.currentTimeMillis();
		
		List<LA_Context> courses = MongoDB_ContextDataProvider.getAllCourses();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("All Courses size '" + courses.size());
		System.out.println("Duration - get all courses: " + secDuration + " ms");
	}
	
	@Test
	public void getLearningObjects() {
		long start = System.currentTimeMillis();
		
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(205);
		List<LA_Object> lObjects = context.getObjects();
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Context learning objects size " + lObjects.size());
		System.out.println("Duration - get context learning objects: " + secDuration + " ms");
	}
	
	//@Test
	public void initializeContextPerformanceTest() {
		long start = System.currentTimeMillis();
		Integer contextID = 212741; // 566475 activities
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		List<LA_Activity> activities = context.getActivities();
		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Initialize Context '" + context.getName() + "' activities: " + activities.size());
		System.out.println("Duration - initialize context '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getStudentsOfCourseTest() {
		long start = System.currentTimeMillis();
		Integer contextID = 212741;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		
		List<LA_Person> students = context.getStudents();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Context '" + context.getName() + "' size students: " + students.size());
		System.out.println("Duration - get students for course '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getInstructorsOfCourseTest() {
		long start = System.currentTimeMillis();
		Integer contextID = 212741;
		MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
		
		List<LA_Person> instructors = context.getInstructors();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Context '" + context.getName() + "' size instructors: " + instructors.size());
		System.out.println("Duration - get instructors for course '" + context.getName() +
				"' : " + secDuration + " ms");
	}
	
	//@Test
	public void getCoursesPerformanceTest() {
		long startTime = System.currentTimeMillis();
		List<LA_Context> courses = dataProvider.getCourses();
		long stopTime = System.currentTimeMillis();
		
		Assert.assertNotNull(courses);
		System.out.println(courses.size());
		System.out.println((stopTime - startTime) / 1000);
	}

}
