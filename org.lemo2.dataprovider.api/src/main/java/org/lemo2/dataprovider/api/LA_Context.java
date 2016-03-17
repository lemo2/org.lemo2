package org.lemo2.dataprovider.api;

import java.util.List;
import java.util.Set;

public interface LA_Context {
	
	/**
	 * 
	 * @return name attribute
	 */
	public String getName();
	
	/**
	 * 
	 * @return descriptor attribute
	 */
	public String getDescriptor();
	
	/**
	 * 
	 * @return names of external attributes
	 */
	public Set<String> extAttributes();
	
	/**
	 * 
	 * @param attr name of external attribute
	 * @return value of external attribute, or null
	 */
	public String getExtAttribute(String attr);
	
	/**
	 * 
	 * @return parent context, or null
	 */
	public LA_Context getParent();
	
	/**
	 * 
	 * @return children contexts
	 */
	public List<LA_Context> getChildren();
	
	/**
	 * 
	 * @return learning objects attached to this context
	 */
	public List<LA_Object> getObjects();
	
	/**
	 * 
	 * @return learning objects attached to this context, or to any context within the child tree
	 */
	public List<LA_Object> getAllObjects();
	
	/**
	 * 
	 * @return persons with role student attached to this context
	 */
	public List<LA_Person> getStudents();
	
	/**
	 * 
	 * @return persons with role instructor attached to this context, or to any context within the child tree
	 */
	public List<LA_Person> getAllStudents();
	
	/**
	 * 
	 * @return persons with role student attached to this context
	 */
	public List<LA_Person> getInstructors();
	
	/**
	 * 
	 * @return persons with role instructor attached to this context, or to any context within the child tree
	 */
	public List<LA_Person> getAllInstructors();
	
	/**
	 * learning activities are sorted by time
	 * @return learning activities attached to this context
	 */
	public List<LA_Activity> getActivities();
	
	/**
	 * learning activities are sorted by time
	 * @return learning activities attached to this context, or to any context within the child tree
	 */
	public List<LA_Activity> getAllActivities();
	
	/**
	 * learning activities are sorted by time
	 * and filtered by person and learning object they refer to,
	 * null values are interpreted as wild cards
	 * @param person referenced by learning activity
	 * @param obj learning object referenced by learning activity
	 * @return filtered learning activities attached to this context
	 */
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj);
	
	/**
	 * learning activities are sorted by time
	 * and filtered by person and learning object they refer to,
	 * null values are interpreted as wild cards
	 * @param person referenced by learning activity
	 * @param obj learning object referenced by learning activity
	 * @return filtered learning activities attached to this context, or to any context within the child tree
	 */
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj);
	
	/**
	 * learning activities are sorted by time
	 * and filtered by person and learning object they refer to,
	 * null values are interpreted as wild cards
	 * @param person referenced by learning activity
	 * @param obj learning object referenced by learning activity
	 * @param start date
	 * @param end date
	 * @return filtered learning activities attached to this context
	 */
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj, long start, long end);
	
	/**
	 * learning activities are sorted by time
	 * and filtered by person and learning object they refer to,
	 * null values are interpreted as wild cards
	 * @param person referenced by learning activity
	 * @param obj learning object referenced by learning activity
	 * @param start date (epoch)
	 * @param end date (epoch)
	 * @return filtered learning activities attached to this context, or to any context within the child tree
	 */
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj, long start, long end);
	
}