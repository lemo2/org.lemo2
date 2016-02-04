package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ContextDataProvider {

	private static Map<Integer, MongoDB_Context> INITIALIZED_CONTEXTS = new HashMap<Integer, MongoDB_Context>();
	
	public static void initializeContext(Integer contextID, MongoDB_Context context) {
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
	
	public static List<LA_Context> getAllCourses() {
		List<LA_Context> courses = new ArrayList<LA_Context>();
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject parentNullQuery = new BasicDBObject();
		parentNullQuery.put("parent", null);
		
		// iterate database results
		DBCursor cursor = contextCollection.find(parentNullQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			Integer contextID = (Integer) obj.get("_id");
			
			// load / create domain object
			MongoDB_Context course = INITIALIZED_CONTEXTS.get(contextID);
			if (course == null) {
				course =  new MongoDB_Context(obj);
			}
			courses.add(course);
		}
		
		return courses;
	}
	
	public static List<LA_Context> getChildrenOfCourse(Integer contextID) {
		List<LA_Context> contextChildren = new ArrayList<LA_Context>();
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject parentNullQuery = new BasicDBObject();
		parentNullQuery.put("parent", contextID);
		
		// iterate children
		DBCursor cursor = contextCollection.find(parentNullQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			Integer childID = (Integer) obj.get("_id");
			
			// load / create domain object
			MongoDB_Context child = INITIALIZED_CONTEXTS.get(childID);
			if (child == null) {
				child = new MongoDB_Context(obj);
			}		
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
			Integer contextID = (Integer) obj.get("_id");
			
			// load / create domain object
			MongoDB_Context course = INITIALIZED_CONTEXTS.get(contextID);
			if (course == null) {
				course =  new MongoDB_Context(obj);
			}
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

}
