package org.lemo2.dataprovider.mongodb.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.MongoDB_ActivityDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_PersonDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Person implements LA_Person {
	
	private Integer personID;
	private String name;
	private String descriptor;
	private Map<String, String> extAttributes;
	private List<LA_Activity> personActivities;
	
	public MongoDB_Person(DBObject personDBObject) {
		this.descriptor = Integer.toString(hashCode());
		this.personID = (Integer) personDBObject.get("_id");
		this.name = (String) personDBObject.get("name");
		this.descriptor = Integer.toString(hashCode());
		
		// initialize person
		extractActivities(personDBObject);
		extractExtentions(personDBObject);
		
		initialize();
	}
	
	private void initialize() {
		MongoDB_PersonDataProvider.initializePerson(this.personID, this);
	}
	
	/**
	 * Extract person activities
	 * @param dbObject
	 */
	private void extractActivities(DBObject personDBObject) {
		List<Integer> activities = (List<Integer>) personDBObject.get("learningActivities");
		
		List<LA_Activity> personActivities = MongoDB_ActivityDataProvider.getActivitiesByIDList(activities);
		this.personActivities = personActivities;
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
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescriptor() {
		return this.descriptor;
	}

	@Override
	public Set<String> extAttributes() {
		return this.extAttributes();
	}

	@Override
	public String getExtAttribute(String attr) {
		if (this.extAttributes == null) {
			return null;
		}
		else {
			return this.extAttributes.get(attr);
		}
	}
	
	/**
	 * Returns the objects of the persons learning activities
	 * @return
	 */
	public List<LA_Activity> getPersonActivities() {
		return this.personActivities;
	}
	
}
