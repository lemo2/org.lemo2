package org.lemo2.dataprovider.api;

import java.util.Set;

public interface DataProvider {
	
	/**
	 * 
	 * course is a LA_Context with no parents
	 * @return all courses
	 */
	public Set<LA_Context> getCourses();
	
	/**
	 * 
	 * @param descriptor attribute for instructor
	 * @return all courses for that instructor
	 */
	public Set<LA_Context> getCoursesByInstructor(String descriptor);
	
	/**
	 * 
	 * @param descriptor attribute for context
	 * @return context identified by descriptor
	 */
	public LA_Context getContext(String descriptor);
	
}