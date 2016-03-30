package org.lemo2.dataprovider.mongodb.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.lemo2.dataprovider.mongodb.MongoDB_PersonDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Context implements LA_Context {
	
	private int contextID;
	private String name;
	private String descriptor;
	private LA_Context parent;
	private Map<String, String> extAttributes;
	private List<LA_Object> contextObjects;
	private List<LA_Context> children;
	private List<LA_Person> students;
	private List<LA_Person> instructors;
	
	private boolean mLock = false;
	
	public MongoDB_Context(DBObject contextObject) {
		this.descriptor = Integer.toString(hashCode());
		this.contextID = (int) contextObject.get("_id");
		this.name = (String) contextObject.get("name");
		
		extractExtentions(contextObject);
		//extractReference(contextObject);
		
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
	   
		if (extList != null) {
		    for (int i = 0; i < extList.size(); i++) {
		    	BSONObject extention = (BSONObject) extList.get(i);
		    	String attr = (String) extention.get("ext_attr");
		    	String value = (String) extention.get("ext_value");
		    	
		    	extAttributes.put(attr, value);
		    }
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
	
	/**
	 * Extracts and builds the learning objects objects from the DBObject. 
	 * @param context
	 */
	private void extractLearningObjects(DBObject context) {
		List<Integer> objectIDs = (List<Integer>) context.get("learningObjects");
	    
		List<LA_Object> contextObjects = MongoDB_ObjectDataProvider.getLearningObjectsByIDList(objectIDs);
		this.contextObjects = contextObjects;
	}
	
	public int getID() {
		return contextID;
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
	public LA_Context getParent() {
		return parent;
	}

	@Override
	public List<LA_Context> getChildren() {
		if (children == null) {
			children = new ArrayList<LA_Context>();
			children = MongoDB_ContextDataProvider.getChildrenTreeOfContext(this, children);
			
		}
		return children;
	}

	@Override
	public List<LA_Object> getObjects() {
		if (contextObjects == null) {
			return MongoDB_ContextDataProvider.getContextLearningObjects(contextID);
		}
		return contextObjects;
	}

	@Override
	public List<LA_Person> getStudents() {
		if (students == null) {
			students = MongoDB_ContextDataProvider.getContextStudents(contextID);
		}
		return students;
	}

	@Override
	public List<LA_Person> getInstructors() {
		if (instructors == null) {
			instructors = MongoDB_ContextDataProvider.getContextInstructors(contextID);
		}
		return instructors;
	}
	
	private void getPersons() {
		instructors = getInstructors();
		students = getStudents();
	}
	
	/**
	 * TODO: überarbeiten...
	 */
	@Override
	public List<LA_Activity> getActivities() {
	
		/*
		if (!isLocked() && !allActivitiesAreInitialized()) {
			mLock = true;
			contextActivities = MongoDB_ActivityDataProvider.getActivities(this);
		}
		
		mLock = false;
		return contextActivities;
		*/
		return MongoDB_ActivityDataProvider.getActivities(this);
	}
	
	public boolean isLocked() {
		return mLock;
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj) {
		return MongoDB_ActivityDataProvider.getActivities(this, person, obj);
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj, long start, long end) {
		return MongoDB_ActivityDataProvider.getActivities(this, person, obj, start, end);
	}

	@Override
	public List<LA_Object> getAllObjects() {
		return MongoDB_ContextDataProvider.getAllLearningObjects(this);
	}

	@Override
	public List<LA_Person> getAllStudents() {
		return MongoDB_ContextDataProvider.getAllStudents(this);
	}

	@Override
	public List<LA_Person> getAllInstructors() {
		return MongoDB_ContextDataProvider.getAllInstructors(this);
	}

	@Override
	public List<LA_Activity> getAllActivities() {
		return MongoDB_ActivityDataProvider.getAllActivities(this);
	}

	@Override
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj) {
		return MongoDB_ActivityDataProvider.getAllActivities(person, obj);
	}

	@Override
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj,
			long start, long end) {
		return MongoDB_ActivityDataProvider.getAllActivities(person, obj, start, end);
	}

}