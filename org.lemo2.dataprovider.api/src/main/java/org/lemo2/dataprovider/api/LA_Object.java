package org.lemo2.dataprovider.api;

import java.util.Set;
import java.util.List;

public interface LA_Object {
		
	/**
	 * 
	 * @return name of object, or null
	 */
	public String getName();
	
	/**
	 * 
	 * @return unique descriptor
	 */
	public String getDescriptor();
	
	/**
	 * 
	 * @return type like quiz, resource, video, ...
	 */
	public String getType();

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
	 * @return parent object
	 */
	public LA_Object getParent();
	
	/**
	 * 
	 * @return list of child objects, or null
	 */
	public List<LA_Object> getChildren();

}
