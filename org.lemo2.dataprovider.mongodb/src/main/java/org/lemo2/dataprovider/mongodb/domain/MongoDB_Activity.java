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

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Activity implements LA_Activity {
	
	private int activityID;
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
		this.activityID = (int) activityObject.get("_id");
		Integer tmpTime = (Integer) activityObject.get("time");
		if (tmpTime != null) {
			this.time = (int) tmpTime;
		}
		this.action = (String) activityObject.get("action");
		this.info = (String) activityObject.get("info");
		
		extractExtensions(activityObject);
		
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
		if (extAttributes == null) {
			return null;
		}
		else {
			return extAttributes.keySet();
		}
	}

	@Override
	public String getExtAttribute(String attrKey) {
		if (extAttributes == null) {
			return null;
		}
		else {
			return extAttributes.get(attrKey);
		}
	}

	@Override
	public LA_Activity getReference() {
		if (reference == null) {
			reference = MongoDB_ActivityDataProvider.getReferenceOfActivity(activityID);
		}
		return reference;
	}

	@Override
	public LA_Object getObject() {
		if (activityObject == null) {
			activityObject = MongoDB_ActivityDataProvider.getLearningObjectOfActivity(activityID);
		}
		return activityObject;
	}

	@Override
	public LA_Person getPerson() {
		if (activityPerson == null) {
			activityPerson = MongoDB_ActivityDataProvider.getPersonOfActivity(activityID);
		}
		return activityPerson;
	}

}
