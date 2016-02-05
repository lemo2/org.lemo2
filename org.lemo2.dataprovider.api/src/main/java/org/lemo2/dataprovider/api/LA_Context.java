package org.lemo2.dataprovider.api;

import java.util.List;
import java.util.Set;

public interface LA_Context {
	
	public String getName();
	
	public String getDescriptor();
	
	public Set<String> extAttributes();
	
	public String getExtAttribute(String attr);
	
	public LA_Context getParent();
	
	public List<LA_Context> getChildren();
	
	public List<LA_Object> getObjects();
	
	public List<LA_Activity> getActivities();
	
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj);
	
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj, long start, long end);
	
	public List<LA_Activity> getActivitiesRecursive();
	
	public List<LA_Activity> getActivitiesRecursive(LA_Person person, LA_Object obj);
	
	public List<LA_Activity> getActivitiesRecursive(LA_Person person, LA_Object obj, long start, long end);
	
	public List<LA_Person> getStudents();
		
	public List<LA_Person> getInstructors();
	
}