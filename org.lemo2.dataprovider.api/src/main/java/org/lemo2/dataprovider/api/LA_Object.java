package org.lemo2.dataprovider.api;

import java.util.Set;
import java.util.List;

public interface LA_Object {
		
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
	 * @return type attribute
	 */
	public String getType();
	
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
	 * @return parent object, or null
	 */
	public LA_Object getParent();
	
	/**
	 * 
	 * @return children objects, or null
	 */
	public List<LA_Object> getChildren();

}
