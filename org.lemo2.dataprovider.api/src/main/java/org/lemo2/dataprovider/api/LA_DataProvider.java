package org.lemo2.dataprovider.api;

import java.util.List;

public interface LA_DataProvider {
	
	/**
	 * 
	 * course is a LA_Context with no parents
	 * @return list of all courses, or null
	 */
	public List<LA_Context> getCourses();
	
	/**
	 * 
	 * @param hashUserId: hash value of userID used on learning platform
	 * @return list of all courses for given instructor, or null
	 */
	public List<LA_Context> getCoursesByInstructor(String hashUserId);
	
	/**
	 * 
	 * @param platformId of course on learning platform (learning environment)
	 * @return course identified by id
	 */
	public LA_Context getCourseByPlatformId(String platformId);
	
	/**
	 * 
	 * @param context descriptor (value returned by method getDescriptor)
	 * @return LA_Context object which has been instantiated by a previous call, or null
	 */
	public LA_Context getContext(String descriptor);
	
	/**
	 * 
	 * @param person descriptor (value returned by method getDescriptor)
	 * @return LA_Person object which has been instantiated by a previous call, or null
	 */
	public LA_Person getPerson(String descriptor);
	
	/**
	 * 
	 * @param object descriptor (value returned by method getDescriptor)
	 * @return LA_Object object which has been instantiated by previous call, or null
	 */
	public LA_Object getObject(String descriptor);
	
}