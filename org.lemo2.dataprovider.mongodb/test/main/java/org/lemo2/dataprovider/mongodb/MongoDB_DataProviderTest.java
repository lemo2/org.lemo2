package org.lemo2.dataprovider.mongodb;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;

public class MongoDB_DataProviderTest {
	
	private MongoDB_DataProvider dataProvider;
	
	private String database = "iversity";
	private String host = "localhost";
	private int port = 27017;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Create MongoDB-Conntection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	
	@Test
	public void getCoursesPerformanceTest() {
		long startTime = System.currentTimeMillis();
		List<LA_Context> courses = dataProvider.getCourses();
		long stopTime = System.currentTimeMillis();
		
		Assert.assertNotNull(courses);
		System.out.println(courses.size());
		System.out.println((stopTime - startTime) / 1000);
	}
	
}
