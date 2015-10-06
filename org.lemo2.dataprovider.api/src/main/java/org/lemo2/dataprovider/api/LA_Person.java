package org.lemo2.dataprovider.api;

import java.util.Set;

public interface LA_Person {
	
	/**
	 * 
	 * @return name of person, or null for anonymous person
	 */
	public String getName();
	
	/**
	 * 
	 * @return unique identifier for person object
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
		
}
