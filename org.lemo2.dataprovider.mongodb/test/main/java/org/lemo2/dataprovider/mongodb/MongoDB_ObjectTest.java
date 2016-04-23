package org.lemo2.dataprovider.mongodb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;

public class MongoDB_ObjectTest {

	private static MongoDB_DataProvider dataProvider;

	private int contextID = 205;
	private int objectID = 121076;
	
	@BeforeClass
	public static void setUp() throws Exception {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		System.out.println("Create MongoDB-Conntection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}
	
	//@Test
	public void getLearningObjectsOfContext() {
		long start = System.currentTimeMillis();
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
		
		List<LA_Object> lObjects = context.getObjects();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Size of learning objects from context " + context.getName() + ": " + lObjects.size());
		System.out.println("Duration - get learning objects " + secDuration + " ms");
	}
	
	@Test
	public void initializeAllLearningObjects() {
		long start = System.currentTimeMillis();
		List<LA_Object> objectIDs = MongoDB_ObjectDataProvider.getAllLearningObjects();

		long end = System.currentTimeMillis();
		long duration = end - start;
		long secDuration = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Size of learning objects: " + objectIDs.size());
		System.out.println("Duration - get all learning objects " + secDuration + " ms");
	}

}
