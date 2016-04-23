package org.lemo2.dataprovider.mongodb.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.MongoDB_PersonDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Person implements LA_Person {
	
	private int personID;
	private String name;
	private String descriptor;
	private Map<String, String> extAttributes;
	
	public MongoDB_Person(DBObject personDBObject) {
		this.descriptor = Integer.toString(hashCode());
		this.personID = (int) personDBObject.get("_id");
		this.name = (String) personDBObject.get("name");
		this.descriptor = Integer.toString(hashCode());
		
		// initialize person
		extractExtentions(personDBObject);
		
		initialize();
	}
	
	private void initialize() {
		MongoDB_PersonDataProvider.initializePerson(this.personID, this);
	}
	
	/**
	 * Extract person extentions
	 * @param dbObject
	 */
	private void extractExtentions(DBObject personDBObject) {
		extAttributes = new HashMap<String, String>();
		
		BasicDBList extList = (BasicDBList) personDBObject.get("extentions");
	   
		if (extList != null) {
		    for (int i = 0; i < extList.size(); i++) {
		    	BSONObject extention = (BSONObject) extList.get(i);
		    	String attr = (String) extention.get("ext_attr");
		    	String value = (String) extention.get("ext_value");
		    	
		    	extAttributes.put(attr, value);
		    }
		}
	}
	
	public int getID() {
		return personID;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescriptor() {
		return descriptor;
	}

	@Override
	public Set<String> extAttributes() {
		if (extAttributes == null) {
			return null;
		}
		return extAttributes.keySet();
	}

	@Override
	public String getExtAttribute(String attr) {
		if (extAttributes == null) {
			return null;
		}
		else {
			return extAttributes.get(attr);
		}
	}
	
	/**
	 * Returns the objects of the persons learning activities
	 * @return
	 */
	public List<LA_Activity> getPersonActivities() {
		return MongoDB_PersonDataProvider.getPersonActivities(personID);
	}
	
}
