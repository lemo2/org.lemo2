package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Activity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ActivityDataProvider {

	private static Map<Integer, MongoDB_Activity> INITIALIZED_ACTIVITIES = new HashMap<Integer, MongoDB_Activity>();
	
	public static void initializeActivity(Integer activityID, MongoDB_Activity activity) {
		INITIALIZED_ACTIVITIES.put(activityID, activity);
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
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", activityID);
			
			DBCursor cursor = collection.find(whereQuery);
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
		
		LA_Activity lActivity;
		
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
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", new BasicDBObject("$in", tmpActivities));
		
		DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			lActivity = new MongoDB_Activity(dbObj);
			activities.add(lActivity);
		}
		
		return activities;
	}
	
	public static LA_Activity getActivityByDescriptor(String descriptor) {
		for (LA_Activity activity : INITIALIZED_ACTIVITIES.values()) {
			if (activity.equals(descriptor)) {
				return activity;
			}
		}
		
		return null;
	}
	
}
