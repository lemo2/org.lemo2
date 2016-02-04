package org.lemo2.dataprovider.mongodb.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.MongoDB_ActivityDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_ObjectDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_PersonDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Activity implements LA_Activity {
	
	private Integer activityID;
	private long time;
	private String action;
	private String info;
	private Map<String, String> extAttributes;
	private LA_Activity reference;
	private LA_Object activityObject;
	private LA_Person activityPerson;
	
	public MongoDB_Activity(List<DBObject> activityObjects) {
		for (DBObject dbObject : activityObjects) {
			new MongoDB_Activity(dbObject);
		}
	}
	
	public MongoDB_Activity(DBObject activityObject) {
		this.activityID = (Integer) activityObject.get("_id");
		this.time = (long) activityObject.get("time");
		this.action = (String) activityObject.get("action");
		this.info = (String) activityObject.get("info");
		
		extractExtensions(activityObject);
		extractReference(activityObject);
		extractLearningObject(activityObject);
		extractPerson(activityObject);
		
		initialize();
	}
	
	private void initialize() {
		MongoDB_ActivityDataProvider.initializeActivity(this.activityID, this);
	}
	
	/**
	 * Extract activity extensions
	 * @param dbObject
	 */
	private void extractExtensions(DBObject activityObject) {
		extAttributes = new HashMap<String, String>();
		
		BasicDBList extList = (BasicDBList) activityObject.get("extentions");
	   
	    for (int i = 0; i < extList.size(); i++) {
	    	BSONObject extention = (BSONObject) extList.get(i);
	    	String attr = (String) extention.get("ext_attr");
	    	String value = (String) extention.get("ext_value");
	    	
	    	extAttributes.put(attr, value);
	    }
	}
	
	/**
	 * Extract reference to another learning activity.
	 * Checks if the referenced learning activity was already initialized. 
	 * If not, load it from the MongoDB.
	 * @param dbObject
	 */
	private void extractReference(DBObject activityObject) {
		Integer referenceID = (Integer) activityObject.get("reference");
		LA_Activity reference = MongoDB_ActivityDataProvider.getActivityByID(referenceID);
		
		this.reference = reference;
	}
	
	/**
	 * Extracts the Learning object.
	 * @param activityObject
	 */
	private void extractLearningObject(DBObject activityObject) {
		Integer objectID = (Integer) activityObject.get("learningObject");
		LA_Object lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(objectID);
		
		this.activityObject = lObject;
	}
	
	private void extractPerson(DBObject activityObject) {
		Integer personID =  (Integer) activityObject.get("personID");
		LA_Person person = MongoDB_PersonDataProvider.loadPersonByID(personID);
		
		this.activityPerson = person;
	}
	
	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public String getAction() {
		return this.action;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public Set<String> extAttributes() {
		if (this.extAttributes == null) {
			return null;
		}
		else {
			return this.extAttributes.keySet();
		}
	}

	@Override
	public String getExtAttribute(String attrKey) {
		if (this.extAttributes == null) {
			return null;
		}
		else {
			return this.extAttributes.get(attrKey);
		}
	}

	@Override
	public LA_Activity getReference() {
		return this.reference;
	}

	@Override
	public LA_Object getObject() {
		return this.activityObject;
	}

	@Override
	public LA_Person getPerson() {
		return this.activityPerson;
	}

}
