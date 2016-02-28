package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.lemo2.dataprovider.api.DataProvider;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;

public class MongoDB_DataProvider implements DataProvider {
	
	@Override
	public List<LA_Context> getCourses() {
		return MongoDB_ContextDataProvider.getAllCourses();
	}

	@Override
	public List<LA_Context> getCoursesByInstructor(String userId) {
		return MongoDB_ContextDataProvider.getCoursesByInstructor(userId);
	}

	@Override
	public LA_Context getContext(String descriptor) {
		return MongoDB_ContextDataProvider.getContextByDescriptor(descriptor);
	}

	@Override
	public LA_Person getPerson(String descriptor) {	
		return MongoDB_PersonDataProvider.getPersonByDescriptor(descriptor);
	}

	@Override
	public LA_Object getObject(String descriptor) {
		return MongoDB_ObjectDataProvider.getObjectByDescriptor(descriptor);
	}
	
	public MongoDB_DataProvider(String host, String databaseName, int databasePort) {
		new MongoDB_Connector(host, databaseName, databasePort);
	}
	
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj, long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		return activities;
	}
	
	public List<LA_Activity> getActivities(String personDescriptor, String objDescriptor, long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		return activities;
	}
	
	public List<LA_Activity> getActivities(int personID, int lObjectID, long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		return activities;
	}

}
