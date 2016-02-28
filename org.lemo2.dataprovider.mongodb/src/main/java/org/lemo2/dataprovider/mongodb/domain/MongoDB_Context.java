package org.lemo2.dataprovider.mongodb.domain;

import java.util.ArrayList;
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
	private List<LA_Activity> contextActivities;
	private List<LA_Person> students;
	private List<LA_Person> instructors;
	
	public MongoDB_Context(DBObject contextObject) {
		this.descriptor = Integer.toString(hashCode());
		this.contextID = (int) contextObject.get("_id");
		this.name = (String) contextObject.get("name");
	
		extractExtentions(contextObject);
		//extractReference(contextObject);
		//extractLearningActivities(contextObject);
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
	 * Extracts and builds the learning activity objects from the DBObject. 
	 * @param context
	 */
	private void extractLearningActivities(DBObject context) {
		List<Integer> activityIDs = (List<Integer>) context.get("learningActivities");
		
		List<LA_Activity> contextActivities = MongoDB_ActivityDataProvider.getActivitiesByIDList(activityIDs);
		this.contextActivities = contextActivities;
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
		return this.contextID;
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
		if (children == null) {
			this.children = MongoDB_ContextDataProvider.getFirstDegreeChildrenOfContext(contextID);
		}
		return this.children;
	}

	@Override
	public List<LA_Object> getObjects() {
		return this.contextObjects;
	}

	/**
	 * TODO: überarbeiten...
	 */
	@Override
	public List<LA_Activity> getActivities() {
		
		removeDuplicates();
		if (!allActivitiesAreInitialized()) {
			this.contextActivities = MongoDB_ActivityDataProvider.getActivities(this);
		}
		
		return this.contextActivities;
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

	@Override
	public List<LA_Person> getStudents() {
		if (this.students == null) {
			this.students = MongoDB_ContextDataProvider.getContextStudents(this.contextID);
		}
		return this.students;
	}

	@Override
	public List<LA_Person> getInstructors() {
		if (this.instructors == null) {
			this.instructors = MongoDB_ContextDataProvider.getContextInstructors(this.contextID);
		}
		return this.instructors;
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
	public List<LA_Activity> getActivitiesRecursive() {
		return MongoDB_ActivityDataProvider.getActivitiesRecursive(this);
	}

	@Override
	public List<LA_Activity> getActivitiesRecursive(LA_Person person, LA_Object obj) {
		return MongoDB_ActivityDataProvider.getActivitiesRecursive(person, obj);
	}

	@Override
	public List<LA_Activity> getActivitiesRecursive(LA_Person person, LA_Object obj, long start, long end) {
		return MongoDB_ActivityDataProvider.getActivitiesRecursive(person, obj, start, end);
	}
	
	private void addActivities(List<LA_Activity> activities) {
		this.contextActivities.addAll(activities);
	}
	
	private void removeDuplicates() {
		
	}
}
