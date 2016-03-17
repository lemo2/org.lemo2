package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Activity;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context2;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context2;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Object;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ActivityDataProvider2 {


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
	
	public static LA_Object getLearningObjectOfActivity(Integer contextID, Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		LA_Object lObject = null;
		Integer lObjectID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", contextID);
		idQuery.put("learningActivities.id", activityID);
		select.put("learningActivities.learningObject", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			lObjectID = (Integer) dbObj.get("learningActivities.learningObject");
			lObject = MongoDB_ObjectDataProvider.getLearningObjectByID(lObjectID);
		}
		
		return lObject;
	}
	
	public static LA_Person getPersonOfActivity(Integer contextID, Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		LA_Person person = null;
		Integer personID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", contextID);
		idQuery.put("learningActivities.id", activityID);
		select.put("learningActivities.person", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			personID = (Integer) dbObj.get("learningActivities.person");
			person = MongoDB_PersonDataProvider.getPersonByID(personID);
		}
		
		return person;
	}
	
	public static LA_Activity getReferenceOfActivity(Integer contextID, Integer activityID) {
		DBCollection collection = MongoDB_Connector.connectToActivityCollection();
		LA_Activity reference = null;
		Integer referenceID = null;
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", contextID);
		idQuery.put("learningActivities.id", activityID);
		select.put("learningActivities.reference", 1); // just return 'reference' field
		
		DBCursor cursor = collection.find(idQuery, select).limit(1); 
		
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			referenceID = (Integer) dbObj.get("learningActivities.reference");
			reference = getActivityByID(referenceID);
		}
		
		return reference;
	}
	
	/**
	 * Returns the list of activity IDs for the given learning context. 
	 * @param context
	 * @return
	 */
	public static List<Integer> getContextActivityIDs(MongoDB_Context2 context) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		List<Integer> activityIDs = new ArrayList<Integer>();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		idQuery.put("_id", context.getID());
		select.put("learningActivities.id", 1);
		
		DBCursor cursor = collection.find(idQuery, select); 
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			activityIDs = (List<Integer>) dbObj.get("learningActivities.id");
		}
		
		return activityIDs;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		
		// load activities from database which are not initialized
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", mContext.getID());
		select.put("learningActivities", 1);
		
		DBCursor cursor = collection.find(idQuery);//, select);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			BasicDBList dbObjList = (BasicDBList) dbObj.get("learningActivities");
			for (Object dbAct : dbObjList) {
				BasicDBObject tmpDBObj = (BasicDBObject) dbAct;
				LA_Activity lActivity = createActivityObject(tmpDBObj);
				activities.add(lActivity);
			}
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person, LA_Object obj, 
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		// choose wildcard function
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;		
			
			activities = getActivitiesNoWildcard(mContext, mPerson, mObj, start, end);
		}
		else if (person != null && obj == null) {
			mPerson = (MongoDB_Person) person;		
			
			activities = getActivitiesWildcardLearningObject(mContext, mPerson, start, end);
		}
		else if (person == null && obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPerson(mContext, mObj, start, end);
		}
		else {
			activities = getActivitiesWildcardPersonAndLearningObject(mContext, start, end);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();

		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;	
			
			activities = getActivitiesWildcardTimeRange(mContext, mPerson, mObj);
		}
		else if (person != null && obj == null) {
			mPerson = (MongoDB_Person) person;		
			
			activities = getActivitiesWildcardLearningObjectAndTimeRange(mContext, mPerson);
		}
		else if (person == null && obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPersonAndTimeRange(mContext, mObj);
		}
		else {
			activities = getActivitiesWildcardPersonAndLearningObjectAndTimerange(mContext);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Person person) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Person mPerson;
		
		if (person != null) {
			mPerson = (MongoDB_Person) person;
			
			activities = getActivitiesWildcardLearningObjectAndTimeRange(mContext, mPerson);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivities(LA_Context context, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Object mObj;
		
		if (obj != null) {
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardPersonAndTimeRange(mContext, mObj);
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesRecursive(MongoDB_Context2 context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<LA_Context> contextTree = new ArrayList<LA_Context>();
		
		// get children tree of the given context
		contextTree = MongoDB_ContextDataProvider2.getChildrenTreeOfContext(context, contextTree);
		contextTree.add(context);
		
		// get the activities for all contexts
		for (LA_Context mContext : contextTree) {
			activities.addAll(getActivities(mContext));
		}
		
		return activities;
	}

	public static List<LA_Activity> getActivitiesRecursive(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;
			
			activities = getActivitiesWildcardTimeRange(mContext, mPerson, mObj);
		}
		
		return activities;
	}

	public static List<LA_Activity> getActivitiesRecursive(LA_Context context, LA_Person person, LA_Object obj, 
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<MongoDB_Object> objectTree = new ArrayList<MongoDB_Object>();
		
		MongoDB_Context2 mContext = (MongoDB_Context2) context;
		MongoDB_Person mPerson;
		MongoDB_Object mObj;
		
		if (person != null && obj != null) {
			mPerson = (MongoDB_Person) person;
			mObj = (MongoDB_Object) obj;
			
			// get parent-children-tree of learning object
			objectTree = MongoDB_ObjectDataProvider.getChildrenTreeOfLearningObject(mObj.getID(), objectTree);
			objectTree.add(mObj);
			
			for (MongoDB_Object tempObj : objectTree) {
				activities.addAll(getActivitiesNoWildcard(mContext, mPerson, tempObj, start, end));
			}
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesNoWildcard(MongoDB_Context2 context, MongoDB_Person person, 
			MongoDB_Object obj,	long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		
		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.person", person.getID());
		selectQuery.put("learningActivities.learningObject", obj.getID());
		selectQuery.put("learningActivities.time", buildTimeRangeQuery(start, end));
		
	    DBCursor cursor = collection.find(selectQuery);
	    
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
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObject(MongoDB_Context2 context, 
			long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		List<Integer> activityIDs = getContextActivityIDs(context);
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		
		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.time", buildTimeRangeQuery(start, end));
		
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity activity = createActivityObject(dbObj);
			contextActivities.add(activity);
		}
	    
		return contextActivities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObjectAndTimerange(MongoDB_Context2 context) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		List<Integer> activityIDs = getContextActivityIDs(context);
		
		List<LA_Activity> contextActivities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		
		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities._id", new BasicDBObject("$in", activityIDs));
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
	private static List<LA_Activity> getActivitiesWildcardLearningObjectAndTimeRange(MongoDB_Context2 context, 
			MongoDB_Person person) {		
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> objectIDs = MongoDB_ContextDataProvider.getContextLearningObjectIDs(context.getID());
		
		BasicDBObject selectQuery = new BasicDBObject();

		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.person", person.getID());
		selectQuery.put("learningActivities.learningObject", new BasicDBObject("$in", objectIDs));
	    
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
	private static List<LA_Activity> getActivitiesWildcardLearningObject(MongoDB_Context2 context,
			MongoDB_Person person, long start, long end) {		
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> objectIDs = MongoDB_ContextDataProvider.getContextLearningObjectIDs(context.getID());
		
		BasicDBObject selectQuery = new BasicDBObject();

		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.person", person.getID());
		selectQuery.put("learningActivities.learningObject", new BasicDBObject("$in", objectIDs));
		selectQuery.put("learningActivities.time", buildTimeRangeQuery(start, end));
	    
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndTimeRange(MongoDB_Context2 context, 
			MongoDB_Object obj) {		
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();

		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.learningObject", obj.getID());
	    
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
	
	private static List<LA_Activity> getActivitiesWildcardPerson(MongoDB_Context2 context, MongoDB_Object obj, 
			long start, long end) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("_id", context.getID());
		selectQuery.put("learningActivities.learningObject", obj.getID());
		selectQuery.put("learningActivities.time", buildTimeRangeQuery(start, end));
	    
	    DBCursor cursor = collection.find(selectQuery);
	    
	    while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			LA_Activity lActivity = createActivityObject(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardTimeRange(MongoDB_Context2 context, 
			MongoDB_Person person, MongoDB_Object obj) {
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
