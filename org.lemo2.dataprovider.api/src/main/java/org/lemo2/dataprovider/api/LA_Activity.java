package org.lemo2.dataprovider.api;

import java.util.Set;

public interface LA_Activity {
	
	/**
	 * 
	 * @return seconds since epoch
	 */
	public long getTime();
	
	/**
	 * 
	 * @return action like view, post, ...
	 */
	public String getAction();
	
	/**
	 * 
	 * @return information uploaded by this activity
	 */
	public String getInfo();

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
	 * @return referenced activity, or null
	 */
	public LA_Activity getReference();
		
	/**
	 * 
	 * @return referenced object, or null
	 */
	public LA_Object getObject();
	
	/**
	 * 
	 * @return referenced person, or null
	 */
	public LA_Person getPerson();
	
}
