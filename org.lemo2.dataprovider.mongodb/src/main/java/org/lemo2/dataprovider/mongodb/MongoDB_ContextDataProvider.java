package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.Document;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context2;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;

public class MongoDB_ContextDataProvider {

	private static Map<Integer, MongoDB_Context> INITIALIZED_CONTEXTS = new HashMap<Integer, MongoDB_Context>();
	
	public static void initializeContext(Integer contextID, MongoDB_Context context) {
		INITIALIZED_CONTEXTS.put(contextID, context);
	}
	
	// TEST
	public static List<Integer> getInitializedIDs() {
		List<Integer> idList = new ArrayList<Integer>();
		idList.addAll(INITIALIZED_CONTEXTS.keySet());
		
		return idList;
	}
	
	/*
	 * FOR TEST
	 */
	public static void clearInitializedContexts() {
		INITIALIZED_CONTEXTS.clear();
	}
	
	/*
	 * FOR TEST
	 */
	public static int getSizeOfInitializedContexts() {
		return INITIALIZED_CONTEXTS.size();
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
	
	public static int countContextActivities(Integer contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextCollection();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", contextID);
		select.put("learningActivities", 1); // just return 'learningActivities' field
		
		DBObject result = collection.findOne(idQuery, select);
		
		try {
			List<Integer> activityIDs = (List<Integer>) result.get("learningActivities");
			return activityIDs.size();
		}
		catch (NullPointerException exc) {
			return 0;
		}

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
	
	public static Set<LA_Context> getAllCourses() {
		Set<LA_Context> courses = new HashSet<LA_Context>();

		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		
		// use no query, so all contexts will be returned
		DBCursor cursor = contextCollection.find();

		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			MongoDB_Context course = createContextObject(dbObj);

			courses.add(course);
		}
	
		return courses;
	}
	
	/**
	 * JUST FOR TEST
	 * Returns recursively all children and their subsequent children of the given context.
	 * @param context
	 * @param children
	 * @return
	 */
	public static List<LA_Context> getChildrenTreeOfContext(MongoDB_Context2 context, List<LA_Context> children)
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
	
	// TODO: TESTDATEN EINFÜGEN --> LOGIN-ID AS EXTENTION
	public static Set<LA_Context> getCoursesByInstructor(String loginID) {
		Set<LA_Context> courses = new HashSet<LA_Context>();
		
		// returns the person which has the given login ID as extention
		MongoDB_Person person = MongoDB_PersonDataProvider.getPersonByLoginID(loginID);
		
		if (person != null) {
			// find all courses
			DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
			BasicDBObject instructorQuery = new BasicDBObject();
			
			instructorQuery.put("persons.person_id", person.getID());
			instructorQuery.put("persons.role", "instructor");
		
			// iterate instructor courses
			DBCursor cursor = contextCollection.find(instructorQuery);
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				MongoDB_Context course = createContextObject(obj);
				
				courses.add(course);
			}
		}
		
		return courses;
	}
	
	/**
	 * Returns the students of the given contexts and its children tree.
	 * @param context
	 * @return
	 */
	public static List<LA_Person> getAllStudents(MongoDB_Context context) {
		List<LA_Person> students = new ArrayList<LA_Person>();
		List<LA_Context> children = new ArrayList<LA_Context>();
		
		Set<LA_Person> studentSet = new HashSet<LA_Person>();
		
		children = getChildrenTreeOfContext(context, children);
		studentSet.addAll(context.getStudents());
		
		for (LA_Context child : children) {
			studentSet.addAll(child.getStudents());
		}
		
		// Set was used to remove duplicates
		students.addAll(studentSet);
		studentSet.clear();
		
		return students;
	}
	
	/**
	 * Returns all persons with the role 'student' which participates in the
	 * course with the given context id.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Person> getContextStudents(Integer contextID) {
		List<LA_Person> students = new ArrayList<LA_Person>();
		List<Integer> personIDs = new ArrayList<Integer>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject query = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		query.put("_id", contextID);
		//query.put("persons.role", "student");
		select.put("persons", 1); // just return 'person_id' field
		
		// iterate results
		DBObject result = contextCollection.findOne(query, select);
		
		try {
			List<DBObject> personsResult = (List<DBObject>) result.get("persons");
			
			// get person ids
			for (DBObject dbObj : personsResult) {
				String role = (String) dbObj.get("role");
				if (role.equals("student")) {
					int pID = (int) dbObj.get("person_id");
					personIDs.add(pID);
				}
			}
			
			students = MongoDB_PersonDataProvider.getPersonsByIDList(personIDs);
		}
		catch(NullPointerException exc) {
			exc.printStackTrace();
		}
		
		return students;
	}
	
	/**
	 * Returns the instructors of the given contexts and its children tree.
	 * @param context
	 * @return
	 */
	public static List<LA_Person> getAllInstructors(MongoDB_Context context) {
		List<LA_Person> instructors = new ArrayList<LA_Person>();
		List<LA_Context> children = new ArrayList<LA_Context>();
		
		Set<LA_Person> instructorSet = new HashSet<LA_Person>();
		
		children = getChildrenTreeOfContext(context, children);
		instructorSet.addAll(context.getInstructors());
		
		for (LA_Context child : children) {
			instructorSet.addAll(child.getInstructors());
		}
		
		// Set was used to remove duplicates
		instructors.addAll(instructorSet);
		instructorSet.clear();
		
		return instructors;
	}
	
	/**
	 * Returns all persons with the role 'instructor' which participates in the
	 * course with the given context id.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Person> getContextInstructors(Integer contextID) {
		List<LA_Person> instructors = new ArrayList<LA_Person>();
		List<Integer> personIDs = new ArrayList<Integer>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject query = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		query.put("_id", contextID);
		//query.put("persons.role", new BasicDBObject("$eq", "instructor"));
		select.put("persons", 1); // just return 'person_id' field
		
		// iterate results
		DBObject result = contextCollection.findOne(query, select);
		
		try {
			List<DBObject> personsResult = (List<DBObject>) result.get("persons");
			
			// get person ids
			for (DBObject dbObj : personsResult) {
				String role = (String) dbObj.get("role");
				if (role.equals("instructor")) {
					int pID = (int) dbObj.get("person_id");
					personIDs.add(pID);
				}
			}
			
			instructors = MongoDB_PersonDataProvider.getPersonsByIDList(personIDs);
		}
		catch(NullPointerException exc) {}
		
		return instructors;
	}
	
	/**
	 * Returns all learning objects attached to this context and to any context within the child tree
	 * @param context
	 * @return
	 */
	public static List<LA_Object> getAllLearningObjects(MongoDB_Context context) {
		List<LA_Object> lObjects = new ArrayList<LA_Object>();
		List<LA_Context> children = new ArrayList<LA_Context>();
		
		Set<LA_Object> lObjectSet = new HashSet<LA_Object>();
		
		children = getChildrenTreeOfContext(context, children);
		lObjectSet.addAll(context.getObjects());
		
		for (LA_Context child : children) {
			lObjectSet.addAll(child.getObjects());
		}
		
		// Set was used to remove duplicates
		lObjects.addAll(lObjectSet);
		lObjectSet.clear();
		
		return lObjects;
	}
	
	/**
	 * Return all learning objects of the given learning context.
	 * Read all learning object IDs of the given context and load the learning objects
	 * from their collection.
	 * @param contextID
	 * @return
	 */
	public static List<LA_Object> getContextLearningObjects(Integer contextID) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		List<Integer> lObjectIDs = new ArrayList<Integer>();
		
		DBCollection contextCollection = MongoDB_Connector.connectToContextCollection();
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", contextID);
		select.put("learningObjects", 1); // just return 'learningObjects' field
		
		DBObject result = contextCollection.findOne(idQuery, select);
		
		lObjectIDs = (List<Integer>) result.get("learningObjects");
		learningObjects = MongoDB_ObjectDataProvider.getLearningObjectsByIDList(lObjectIDs);
			
		return learningObjects;
	}
	
	/**
	 * Returns a context which was already initialized which has the given descriptor.
	 * @param descriptor
	 * @return
	 */
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
