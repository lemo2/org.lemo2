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
import org.lemo2.dataprovider.mongodb.MongoDB_ActivityDataProvider2;
import org.lemo2.dataprovider.mongodb.MongoDB_ContextDataProvider2;
import org.lemo2.dataprovider.mongodb.MongoDB_ContextDataProvider;
import org.lemo2.dataprovider.mongodb.MongoDB_ObjectDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Context2 implements LA_Context {

	private int contextID;
	private String name;
	private String descriptor;
	private LA_Context parent;
	private Map<String, String> extAttributes;
	private List<LA_Object> contextObjects;
	private List<LA_Context> children;
	private List<LA_Activity> contextActivities;
	private List<LA_Person> students;
	private List<LA_Person> instructors;
	
	public MongoDB_Context2(DBObject contextObject) {
		this.descriptor = Integer.toString(hashCode());
		this.contextID = (int) contextObject.get("_id");
		this.name = (String) contextObject.get("name");
	
		extractExtentions(contextObject);
		//extractLearningObjects(contextObject);
		
		initialize();
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
	 * Extracts and builds the learning objects objects from the DBObject. 
	 * @param context
	 */
	private void extractLearningObjects(DBObject context) {
		List<Integer> objectIDs = (List<Integer>) context.get("learningObjects");
	    
		List<LA_Object> contextObjects = MongoDB_ObjectDataProvider.getLearningObjectsByIDList(objectIDs);
		this.contextObjects = contextObjects;
	}
	
	private void initialize() {
		MongoDB_ContextDataProvider2.initializeContext(this.contextID, this);
	}
	
	public Integer getID() {
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
	public List<LA_Activity> getActivities() {
		if (!allActivitiesAreInitialized()) {
			this.contextActivities = MongoDB_ActivityDataProvider2.getActivities(this);
		}
		
		removeActivityDuplicates();
		return contextActivities;
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj) {
		return MongoDB_ActivityDataProvider2.getActivities(this, person, obj);
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj,
			long start, long end) {
		return MongoDB_ActivityDataProvider2.getActivities(this, person, obj, start, end);
	}

	@Override
	public List<LA_Activity> getActivitiesRecursive() {
		return MongoDB_ActivityDataProvider2.getActivitiesRecursive(this);
	}

	@Override
	public List<LA_Activity> getActivitiesRecursive(LA_Person person,
			LA_Object obj) {
		return MongoDB_ActivityDataProvider2.getActivitiesRecursive(this, person, obj);
	}

	@Override
	public List<LA_Activity> getActivitiesRecursive(LA_Person person,
			LA_Object obj, long start, long end) {
		return MongoDB_ActivityDataProvider2.getActivitiesRecursive(this, person, obj, start, end);
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
	
	/**
	 * Checks if all of the context activities are already initialized.
	 * @return
	 */
	private boolean allActivitiesAreInitialized() {
		if (this.contextActivities == null) {
			return false;
		}
		else {
			List<Integer> activityIDs = MongoDB_ContextDataProvider.getContextActivityIDs(this.contextID);
			if (this.contextActivities.size() != activityIDs.size()) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	private void removeActivityDuplicates() {
		Set<LA_Activity> setActivities = new HashSet<LA_Activity>(contextActivities);
		contextActivities.clear();
		contextActivities.addAll(setActivities);
	}

}
