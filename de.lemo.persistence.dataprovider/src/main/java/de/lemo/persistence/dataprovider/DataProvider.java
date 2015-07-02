package de.lemo.persistence.dataprovider;

import java.util.Set;

public interface DataProvider {
	
	/**
	 * 
	 * course is a ED_Context with no parents
	 * @return set of all courses, or null, if there is no database connection
	 */
	public Set<ED_Context> getCourses();
	
	/**
	 * 
	 * @param String identifying instructor (e.g. userID)
	 * @return set of all courses for instructor, or null
	 */
	public Set<ED_Context> getCoursesByInstructor(String userId);
	
	/**
	 * 
	 * @param context descriptor (value returned by method getDescriptor)
	 * @return ED_Context object which has been instantiated by a previous call, or null
	 */
	public ED_Context getContext(String descriptor);
	
	/**
	 * 
	 * @param person descriptor (value returned by method getDescriptor)
	 * @return ED_Person object which has been instantiated by a previous call, or null
	 */
	public ED_Person getPerson(String descriptor);
	
	/**
	 * 
	 * @param object descriptor (value returned by method getDescriptor)
	 * @return ED_Object object which has been instantiated by previous call, or null
	 */
	public ED_Object getObject(String descriptor);
	
}