package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Activity;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Object;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ActivityDataProvider {

	private static Map<Integer, MongoDB_Activity> INITIALIZED_ACTIVITIES = new HashMap<Integer, MongoDB_Activity>();
	
	public static void initializeActivity(Integer activityID, MongoDB_Activity activity) {
		INITIALIZED_ACTIVITIES.put(activityID, activity);
	}
	
	/**
	 * JUST FOR PERFORMANCE-TEST PURPOSE
	 */
	public static void clearInitializedActivities() {
		INITIALIZED_ACTIVITIES.clear();
	}
	
	/*
	 * FOR TEST
	 */
	public static int getSizeOfInitializedActivities() {
		return INITIALIZED_ACTIVITIES.size();
	}
	
	// For test
	public static List<Integer> getAllLearningActivityIDs() {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<Integer> activityIDs = new ArrayList<Integer>();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		select.put("_id", 1);
		
		DBCursor cursor = collection.find(idQuery, select);
		
		int activityID;
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			activityID = (Integer) dbObj.get("_id");
			activityIDs.add(activityID);
		}
		
		return activityIDs;
	}
	
	public static int countActivitiesOfContext(Integer contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		List<Integer> activityIDs = new ArrayList<Integer>();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", contextID);
		select.put("learningActivities", 1);
		
		DBCursor cursor = collection.find(idQuery, select); 
		
		return cursor.size();
	}
	
	public static LA_Object getLearningObjectOfActivity(Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		LA_Object lObject = null;
		Integer lObjectID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", activityID);
		select.put("learningObject", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			lObjectID = (Integer) dbObj.get("learningObject");
			lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(lObjectID);
		}
		
		return lObject;
	}
	
	public static LA_Person getPersonOfActivity(Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		LA_Person person = null;
		Integer personID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", activityID);
		select.put("person", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			personID = (Integer) dbObj.get("person");
			person = MongoDB_PersonDataProvider.getPersonByID(personID);
		}
		
		return person;
	}
	
	public static LA_Activity getReferenceOfActivity(Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		LA_Activity reference = null;
		Integer referenceID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", activityID);
		select.put("reference", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			referenceID = (Integer) dbObj.get("reference");
			reference = getActivityByID(referenceID);
		}
		
		return reference;
	}
	
	/**
	 * Returns the list of activity IDs for the given learning context. 
	 * @param context
	 * @return
	 */
	// TODO: context to ID?
	public static List<Integer> getContextActivityIDs(MongoDB_Context context) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		List<Integer> activityIDs = new ArrayList<Integer>();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", context.getID());
		select.put("learningActivities", 1);
		
		DBCursor cursor = collection.find(idQuery, select); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			activityIDs = (List<Integer>) dbObj.get("learningActivities");
		}
		
		return activityIDs;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		MongoDB_Context mContext = (MongoDB_Context) context;
		
		List<Integer> activityIDs = MongoDB_ContextDataProvider.getContextActivityIDs(mContext.getID());
		activities = MongoDB_ActivityDataProvider.getActivitiesByIDList(activityIDs);
		
		return activities;
	}
	
	// TEST
	public static List<DBObject> getActivities_Test(LA_Context context) {
		List<DBObject> activities = new ArrayList<DBObject>();
		MongoDB_Context mContext = (MongoDB_Context) context;
		
		List<Integer> activityIDs = MongoDB_ContextDataProvider.getContextActivityIDs(mContext.getID());
		activities = MongoDB_ActivityDataProvider.getActivitiesByIDList_DBObjects(activityIDs);
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person, LA_Object obj, 
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context mContext = (MongoDB_Context) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		// choose wildcard function
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;		
			
			activities = getActivitiesNoWildcard(mPerson, mObj, start, end);
		}
		else if (person != null && obj == null) {
			mPerson = (MongoDB_Person) person;		
			
			activities = getActivitiesWildcardLearningObject(mContext, mPerson, start, end);
		}
		else if (person == null && obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPerson(mObj, start, end);
		}
		else {
			activities = getActivitiesWildcardPersonAndLearningObject(mContext, start, end);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();

		MongoDB_Context mContext = (MongoDB_Context) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		// choose wildcard function
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;	
			
			activities = getActivitiesWildcardTimeRange(mPerson, mObj);
		}
		else if (person != null && obj == null) {
			mPerson = (MongoDB_Person) person;		
			
			activities = getActivitiesWildcardLearningObjectAndTimeRange(mContext, mPerson);
		}
		else if (person == null && obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPersonAndTimeRange(mObj);
		}
		else {
			activities = getActivitiesWildcardPersonAndLearningObjectAndTimerange(mContext);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context mContext = (MongoDB_Context) context;
		MongoDB_Person mPerson;
		
		if (person != null) {
			mPerson = (MongoDB_Person) person;
			
			activities = getActivitiesWildcardLearningObjectAndTimeRange(mContext, mPerson);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Object mObj;
		
		if (obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPersonAndTimeRange(mObj);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getAllActivities(MongoDB_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<LA_Context> contextTree = new ArrayList<LA_Context>();
		
		// get children tree of the given context
		contextTree = MongoDB_ContextDataProvider.getChildrenTreeOfContext(context, contextTree);
		contextTree.add(context);
		
		// get the activities for all contexts
		for (LA_Context mContext : contextTree) {
			activities.addAll(getActivities(mContext));
		}
		
		return activities;
	}

	public static List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<MongoDB_Object> objectTree = new ArrayList<MongoDB_Object>();
		
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;
			
			// get parent child tree of learning object
			objectTree = MongoDB_ObjectDataProvider.getChildrenTreeOfLearningObject(mObj.getID(), objectTree);
			objectTree.add(mObj);
			
			// get all activities for the person and the learning objects
			for (MongoDB_Object tmpObj : objectTree) {
				activities.addAll(getActivitiesWildcardTimeRange(mPerson, tmpObj));
			}
		}
		
		return activities;
	}

	public static List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj, 
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<MongoDB_Object> objectTree = new ArrayList<MongoDB_Object>();
		
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;
			
			// get parent child tree of learning object
			objectTree = MongoDB_ObjectDataProvider.getChildrenTreeOfLearningObject(mObj.getID(), objectTree);
			objectTree.add(mObj);
			
			for (MongoDB_Object tmpObj : objectTree) {
				activities.addAll(getActivitiesNoWildcard(mPerson, tmpObj, start, end));
			}
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesNoWildcard(MongoDB_Person person, MongoDB_Object obj,
			long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject();
		
		selectQuery.put("person", person.getID());
		selectQuery.put("learningObject", obj.getID());
		selectQuery.put("time", buildTimeRangeQuery(start, end));
		sortQuery.put("time", 1);
		
	    DBCursor cursor = collection.find(selectQuery).sort(sortQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity activity = createActivityObject(dbObj);		
			contextActivities.add(activity);
		}
	    
		return contextActivities;
	}
	
	/**
	 * Returns all learning activities for the given context, which took part in the given timeperiod.
	 * @param context
	 * @param start
	 * @param end
	 * @return
	 */
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObject(MongoDB_Context context, 
			long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		List<Integer> activityIDs = getContextActivityIDs(context);
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject();
		
		selectQuery.put("time", buildTimeRangeQuery(start, end));
		selectQuery.put("_id", new BasicDBObject("$in", activityIDs));
		sortQuery.put("time", 1);
		
	    DBCursor cursor = collection.find(selectQuery).sort(sortQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity activity = createActivityObject(dbObj);
			contextActivities.add(activity);
		}
	    
		return contextActivities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObjectAndTimerange(MongoDB_Context context) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		List<Integer> activityIDs = getContextActivityIDs(context);
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		
		selectQuery.put("_id", new BasicDBObject("$in", activityIDs));
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity activity = createActivityObject(dbObj);
			contextActivities.add(activity);
		}
	    
		return contextActivities;
	}
	
	/**
	 * TODO: weg?
	 * @param person
	 * @param contextID
	 * @return
	 */
	private static List<LA_Activity> getActivitiesWildcardLearningObjectAndTimeRange(MongoDB_Context context, 
			MongoDB_Person person) {		
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> objectIDs = MongoDB_ContextDataProvider.getContextLearningObjectIDs(context.getID());
		
		BasicDBObject selectQuery = new BasicDBObject();

		selectQuery.put("person", person.getID());
		selectQuery.put("learningObject", new BasicDBObject("$in", objectIDs));
	    
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	/**
	 * TODO: weg?
	 * @param person
	 * @param contextID
	 * @return
	 */
	private static List<LA_Activity> getActivitiesWildcardLearningObject(MongoDB_Context context,
			MongoDB_Person person, long start, long end) {		
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> objectIDs = MongoDB_ContextDataProvider.getContextLearningObjectIDs(context.getID());
		
		BasicDBObject selectQuery = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject();

		selectQuery.put("person", person.getID());
		selectQuery.put("learningObject", new BasicDBObject("$in", objectIDs));
		selectQuery.put("time", buildTimeRangeQuery(start, end));
	    sortQuery.put("time", 1);
		
	    DBCursor cursor = collection.find(selectQuery).sort(sortQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndTimeRange(MongoDB_Object obj) {		
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();

		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("learningObject", obj.getID());
	    
	    DBCursor cursor = collection.find(selectQuery);
	    
	    LA_Activity[] activityArr = new LA_Activity[cursor.size()];
	    int i = 0;
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			//activities.add(createActivityObject(dbObj));
			activityArr[i] = createActivityObject(dbObj);
		}
		
		return new ArrayList<LA_Activity>(Arrays.asList(activityArr));
	}
	
	private static List<LA_Activity> getActivitiesWildcardPerson(MongoDB_Object obj, long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject();
		
		selectQuery.put("learningObject", obj.getID());
		selectQuery.put("time", buildTimeRangeQuery(start, end));
	    sortQuery.put("time", 1);
		
	    DBCursor cursor = collection.find(selectQuery).sort(sortQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardTimeRange(MongoDB_Person person, MongoDB_Object obj) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		
		selectQuery.put("person", person.getID());
		selectQuery.put("learningObject", obj.getID());
	    
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	/**
	 * Builds and returns a query based on the given values.
	 * @param start
	 * @param end
	 * @return
	 */
	private static DBObject buildTimeRangeQuery(long start, long end) {
		DBObject dbObj;
		if (start > 0 && end > 0) {
			return BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get();
		}
		else if (start > 0 && end == 0) {
			return BasicDBObjectBuilder.start("$gte", start).get();
		}
		else if (start == 0 && end > 0) {
			return BasicDBObjectBuilder.start("$lte", end).get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Loads a learning activity from the given ID. 
	 * It will be checked, if the learning activity was already loaded and initialized from
	 * the database.
	 * @param activityID
	 * @return
	 */
	public static LA_Activity getActivityByID(Integer activityID) {
		MongoDB_Activity activity = INITIALIZED_ACTIVITIES.get(activityID);
		
		// Learning activity is not yet initialized
		if (activity == null) {
			DBCollection collection = MongoDB_Connector.connectToActivityCollection();
			
			BasicDBObject selectQuery = new BasicDBObject();
			selectQuery.put("_id", activityID);
			
			DBCursor cursor = collection.find(selectQuery);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				activity = new MongoDB_Activity(dbObj);
			}
		}
		
		return activity;
	}
	
	/**
	 * Load LA_Activity objects by the ID list.
	 * @param activityIDs
	 * @return
	 */
	public static List<LA_Activity> getActivitiesByIDList(List<Integer> activityIDs) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> tmpActivities = new ArrayList<Integer>();
		
		MongoDB_Activity lActivity;
		
		// Load initialized activities
		for (int aID : activityIDs) {
			lActivity = INITIALIZED_ACTIVITIES.get(aID); 
			if (lActivity != null) {
				activities.add(lActivity);
			}
			else {
				tmpActivities.add(aID);
			}
		}
		
		// load activities from database which are not initialized
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("_id", new BasicDBObject("$in", tmpActivities));
		
		DBCursor cursor = collection.find(selectQuery);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			lActivity = new MongoDB_Activity(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	public static List<DBObject> getActivitiesByIDList_DBObjects(List<Integer> activityIDs) {
		List<DBObject> activities = new ArrayList<DBObject>();
		
		// load activities from database which are not initialized
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("_id", new BasicDBObject("$in", activityIDs));
		
		DBCursor cursor = collection.find(selectQuery);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			activities.add(dbObj);
		}
		
		return activities;
	}
	
	/**
	 * Does a lookup in a HashMap for the descriptor (key) and returns the initialized activity.
	 * @param descriptor
	 * @return
	 */
	public static MongoDB_Activity getActivityByDescriptor(String descriptor) {
		for (MongoDB_Activity activity : INITIALIZED_ACTIVITIES.values()) {
			if (activity.equals(descriptor)) {
				return activity;
			}
		}
		
		return null;
	}
	
	private static MongoDB_Activity createActivityObject(DBObject dbObject) {
		Integer activityID = (Integer) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Activity activity = INITIALIZED_ACTIVITIES.get(activityID);
		if (activity == null) {
			activity = new MongoDB_Activity(dbObject);
		}
		
		return activity;
	}
	
}
