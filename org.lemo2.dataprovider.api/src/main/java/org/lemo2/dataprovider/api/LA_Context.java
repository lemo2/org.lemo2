package org.lemo2.dataprovider.api;

import java.util.List;
import java.util.Set;

public interface LA_Context {
	
	/**
	 * 
	 * @return name of context, or null
	 */
	public String getName();
	
	/**
	 * 
	 * @return unique descriptor
	 */
	public String getDescriptor();

	/**
	 * 
	 * @return names of all external attributes, or null
	 */
	public Set<String> getExtNames();
	
	/**
	 * 
	 * @param name
	 * @return value of external attribute, or null
	 */
	public String getExtValue(String name);
		
	/**
	 * 
	 * @return parent context, or null
	 */
	public LA_Context getParent();
	
	/**
	 * 
	 * @return list of child contexts, or null
	 */
	public List<LA_Context> getChildren();
	
	/**
	 * 
	 * @return list of objects associated with this context (not recursive)
	 */
	public List<LA_Object> getObjects();
	
	/**
	 * 
	 * @return list of activities associated with this context (not recursive)
	 */
	public List<LA_Activity> getActivities();
	
	/**
	 * 
	 * @return list of students associated with this context (not recursive)
	 */
	public List<LA_Person> getStudents();
		
	/**
	 * 
	 * @return list of instructors associated with this context (not recursive)
	 */
	public List<LA_Person> getInstructors();
	
}