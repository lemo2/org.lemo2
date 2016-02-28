package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

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
			
			DBCursor cursor = collection.find(whereQuery).limit(1);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				context = new MongoDB_Context(dbObj);
			}
		}
		
		return context;
	}
	
	/**
	 * Returns all Learning Activity IDs of the given Learning Context.
	 * @param contextID
	 * @return
	 */
	public static List<Integer> getContextActivityIDs(Integer contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", contextID);
		select.put("learningActivities", 1); // just return 'learningActivities' field
		
		DBObject result = collection.findOne(idQuery, select);
		
		List<Integer> activityIDs = (List<Integer>) result.get("learningActivities");
		
		return activityIDs;
	}
	
	/**
	 * Returns all Learning Object IDs of the given Learning Context.
	 * @param contextID
	 * @return
	 */
	public static List<Integer> getContextLearningObjectIDs(Integer contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", contextID);
		DBObject result = collection.findOne(whereQuery);
		
		List<Integer> objectIDs = (List<Integer>) result.get("learningObjects");
		
		return objectIDs;
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
			DBObject dbObj = cursor.next();
			Integer contextID = (Integer) dbObj.get("_id");
			
			// Create parent object and its children
			MongoDB_Context course = createContextObject(dbObj);
			List<LA_Context> children = new ArrayList<LA_Context>();
			children = getChildrenTreeOfContext(course, children);

			courses.add(course);
			courses.addAll(children);
		}
	
		return courses;
	}
	
	/**
	 * Returns recursively all children and their subsequent children of the given context.
	 * @param context
	 * @param children
	 * @return
	 */
	public static List<LA_Context> getChildrenTreeOfContext(MongoDB_Context context, List<LA_Context> children)
	{
	   for( LA_Context child : getFirstDegreeChildrenOfContext(context.getID()) )
	   {
		   MongoDB_Context mChild = (MongoDB_Context) child;
		   children.add(child);
		   getChildrenTreeOfContext(mChild, children);
	   }
	   return children;
	}
	
	/**
	 * Returns a list of Learning contexts which refer to the given context id as parent.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Context> getFirstDegreeChildrenOfContext(Integer contextID) {
		List<LA_Context> contextChildren = new ArrayList<LA_Context>();
		
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
	
	// Überarbeiten!!!!
	public static List<LA_Context> getCoursesByInstructor(String loginID) {
		List<LA_Context> courses = new ArrayList<LA_Context>();
		MongoDB_Person person = MongoDB_PersonDataProvider.getPersonByLoginID(loginID);
		
		Integer instructorID = Integer.valueOf(loginID);
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject instructorQuery = new BasicDBObject();
		
		if (person != null) {
			instructorQuery.put("persons.person_id", person.getID());
			instructorQuery.put("persons.role", "instructor");
		}
		else {
			
		}
		
		// iterate instructor courses
		DBCursor cursor = contextCollection.find(instructorQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			MongoDB_Context course = createContextObject(obj);
			
			courses.add(course);
		}
		
		return courses;
	}
	
	/**
	 * Returns all persons with the role 'student' which participates in the
	 * course with the given context id.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Person> getContextStudents(Integer contextID) {
		List<LA_Person> students = new ArrayList<LA_Person>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("context", contextID);
		selectQuery.put("persons.role", "student");
		
		// iterate students
		DBCursor cursor = contextCollection.find(selectQuery);
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			MongoDB_Person person = MongoDB_PersonDataProvider.createPersonObject(dbObj);
			
			students.add(person);
		}
		
		return students;
	}
	
	/**
	 * Returns all persons with the role 'instructor' which participates in the
	 * course with the given context id.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Person> getContextInstructors(Integer contextID) {
		List<LA_Person> instructors = new ArrayList<LA_Person>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject selectQuery = new BasicDBObject();
		selectQuery.put("context", contextID);
		selectQuery.put("persons.role", "instructor");
		
		// iterate students
		DBCursor cursor = contextCollection.find(selectQuery);
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			MongoDB_Person person = MongoDB_PersonDataProvider.createPersonObject(dbObj);
			
			instructors.add(person);
		}
		
		return instructors;
	}
	
	public static List<LA_Object> getContextLearningObjects(Integer contextID) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		List<Integer> lObjectIDs = new ArrayList<Integer>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", contextID);
		select.put("learningObjects", 1); // just return 'learningActivities' field
		
		DBObject result = contextCollection.findOne(idQuery, select);
		
		lObjectIDs = (List<Integer>) result.get("learningObjects");
		learningObjects = MongoDB_ObjectDataProvider.getLearningObjectsByIDList(lObjectIDs);
			
		return learningObjects;
	}
	
	public static MongoDB_Context getContextByDescriptor(String descriptor) {

		for (MongoDB_Context context : INITIALIZED_CONTEXTS.values()) {
			if (context.equals(descriptor)) {
				return context;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if the given learning context was already initialized. If not 
	 * create a new 'MongoDB_Context' instance.
	 * @param dbObject
	 * @return
	 */
	private static MongoDB_Context createContextObject(DBObject dbObject) {
		Integer contextID = (Integer) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Context context = INITIALIZED_CONTEXTS.get(contextID);
		if (context == null) {
			context = new MongoDB_Context(dbObject);
		}
		
		return context;
	}

}
