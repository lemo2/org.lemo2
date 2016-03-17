package org.lemo2.dataprovider.api;

import java.util.Set;

public interface LA_Activity {
	
	/**
	 * 
	 * @return time stamp (epoch)
	 */
	public long getTime();
	
	/**
	 * 
	 * @return action attribute
	 */
	public String getAction();
	
	/**
	 * 
	 * @return info attribute
	 */
	public String getInfo();
	
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
	 * @return activity referred to by this activity, or null
	 */
	public LA_Activity getReference();
		
	/**
	 * 
	 * @return learning object referenced by this activity
	 */
	public LA_Object getObject();
	
	/**
	 * 
	 * @return person referenced by this activity
	 */
	public LA_Person getPerson();
	
}
