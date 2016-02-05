package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ContextDataProvider {

	private static Map<Long, MongoDB_Context> INITIALIZED_CONTEXTS = new HashMap<Long, MongoDB_Context>();
	
	public static void initializeContext(Long contextID, MongoDB_Context context) {
		INITIALIZED_CONTEXTS.put(contextID, context);
	}
	
	public static LA_Context getContextByID(Integer contextID) {
		MongoDB_Context context = INITIALIZED_CONTEXTS.get(contextID);
		
		// Learning context is not yet initialized
		if (context == null) {
			DBCollection collection = MongoDB_Connector.connectToContextCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", contextID);
			
			DBCursor cursor = collection.find(whereQuery);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				context = new MongoDB_Context(dbObj);
			}
		}
		
		return context;
	}
	
	public static List<Integer> getContextActivityIDs(Long contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", contextID);
		DBObject result = collection.findOne(whereQuery);
		
		List<Integer> activityIDs = (List<Integer>) result.get("learningActivities");
		
		return activityIDs;
	}
	
	
	public static List<LA_Context> getAllCourses() {
		List<LA_Context> courses = new ArrayList<LA_Context>();
		
		// find all parent courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject parentNullQuery = new BasicDBObject();
		parentNullQuery.put("parent", 0);
		
		// iterate database results
		DBCursor cursor = contextCollection.find(parentNullQuery);

		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			Long contextID = (Long) obj.get("_id");
			
			// Create parent object and its children
			MongoDB_Context course = createContextObject(obj);
			List<MongoDB_Context> children = new ArrayList<MongoDB_Context>();
			children = getChildrenTreeOfCourse(contextID, children);

			courses.add(course);
			courses.addAll(children);
		}
	
		return courses;
	}
	
	private static List<MongoDB_Context> getChildrenTreeOfCourse(Long contextID, List<MongoDB_Context> children)
	{
	   for( MongoDB_Context child : getChildrenOfCourse(contextID) )
	   {
		   children.add(child);
		   getChildrenTreeOfCourse(child.getID(), children);
	   }
	   return children;
	}
	
	private static List<MongoDB_Context> getChildrenOfCourse(Long contextID) {
		List<MongoDB_Context> contextChildren = new ArrayList<MongoDB_Context>();
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject parentNullQuery = new BasicDBObject();
		parentNullQuery.put("parent", contextID);
		
		// iterate children
		DBCursor cursor = contextCollection.find(parentNullQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			MongoDB_Context child = createContextObject(obj);
			
			contextChildren.add(child);
		}
	
		return contextChildren;
	}
	
	public static List<LA_Context> getCoursesByInstructor(String userId) {
		List<LA_Context> courses = new ArrayList<LA_Context>();
		
		Long instructorID = Long.valueOf(userId);
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject instructorQuery = new BasicDBObject();
		instructorQuery.put("person_id", instructorID);
		instructorQuery.put("persons.role", "instructor");
		
		// iterate instructor courses
		DBCursor cursor = contextCollection.find(instructorQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			LA_Context course = createContextObject(obj);
			
			courses.add(course);
		}
		
		return courses;
	}
	
	public static LA_Context getContextByDescriptor(String descriptor) {

		for (LA_Context context : INITIALIZED_CONTEXTS.values()) {
			if (context.equals(descriptor)) {
				return context;
			}
		}
		
		return null;
	}
	
	private static MongoDB_Context createContextObject(DBObject dbObject) {
		Integer childID = (Integer) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Context context = INITIALIZED_CONTEXTS.get(childID);
		if (context == null) {
			context = new MongoDB_Context(dbObject);
		}
		
		return context;
	}

}
