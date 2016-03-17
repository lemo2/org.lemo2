package org.lemo2.dataprovider.api;

import java.util.Set;

public interface LA_Person {
	
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
		
}
