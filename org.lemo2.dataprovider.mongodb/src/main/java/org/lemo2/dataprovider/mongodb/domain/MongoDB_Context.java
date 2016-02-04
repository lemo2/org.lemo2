package org.lemo2.dataprovider.mongodb.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.MongoDB_ActivityDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_ContextDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_ObjectDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Context implements LA_Context {
	
	private Integer contextID;
	private String name;
	private String descriptor;
	private LA_Context parent;
	private Map<String, String> extAttributes;
	private List<LA_Object> contextObjects;
	private List<LA_Context> children;
	private List<LA_Activity> contextActivities;
	private List<LA_Person> students;
	private List<LA_Person> instructors;
	
	public MongoDB_Context(DBObject contextObject) {
		this.descriptor = Integer.toString(hashCode());
		this.contextID = (Integer) contextObject.get("_id");
		this.name = (String) contextObject.get("name");
	
		extractExtentions(contextObject);
		extractReference(contextObject);
		extractLearningActivities(contextObject);
		extractLearningObjects(contextObject);
		
		initialize();
	} 
	
	private void initialize() {
		MongoDB_ContextDataProvider.initializeContext(this.contextID, this);
	}
	
	/**
	 * 
	 * @param contextObject - Learning context DBObject
	 */
	private void extractExtentions(DBObject contextObject) {
		extAttributes = new HashMap<String, String>();
		
		BasicDBList extList = (BasicDBList) contextObject.get("extentions");
	   
	    for (int i = 0; i < extList.size(); i++) {
	    	BSONObject extention = (BSONObject) extList.get(i);
	    	String attr = (String) extention.get("ext_attr");
	    	String value = (String) extention.get("ext_value");
	    	
	    	extAttributes.put(attr, value);
	    }
	}
	
	/**
	 * Extract parent reference to another learning context.
	 * Checks if the referenced learning context was already initialized. 
	 * If not, load it from the MongoDB.
	 * @param dbObject - Learning context DBObject 
	 */
	private void extractReference(DBObject contextObject) {
		Integer parentID = (Integer) contextObject.get("reference");
		
		LA_Context parent = MongoDB_ContextDataProvider.getContextByID(parentID);
		this.parent = parent;
	}
	
	private void extractLearningActivities(DBObject context) {
		List<Integer> activityIDs = (List<Integer>) context.get("learningActivities");
		
		List<LA_Activity> contextActivities = MongoDB_ActivityDataProvider.getActivitiesByIDList(activityIDs);
		this.contextActivities = contextActivities;
	}
	
	private void extractLearningObjects(DBObject context) {
		List<Integer> objectIDs = (List<Integer>) context.get("learningObjects");
	    
		List<LA_Object> contextObjects = MongoDB_ObjectDataProvider.getLearningObjectsByIDList(objectIDs);
		this.contextObjects = contextObjects;
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
	public LA_Context getParent() {
		return this.parent;
	}

	@Override
	public List<LA_Context> getChildren() {
		return this.children;
	}

	@Override
	public List<LA_Object> getObjects() {
		return this.contextObjects;
	}

	@Override
	public List<LA_Activity> getActivities() {
		return this.contextActivities;
	}

	@Override
	public List<LA_Person> getStudents() {
		return this.students;
	}

	@Override
	public List<LA_Person> getInstructors() {
		return this.instructors;
	}
	
}
