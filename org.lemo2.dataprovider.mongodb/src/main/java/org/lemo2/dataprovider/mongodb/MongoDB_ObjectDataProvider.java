package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Object;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ObjectDataProvider {

	public static Map<Integer, MongoDB_Object> INITIALIZED_OBJECTS = new HashMap<Integer, MongoDB_Object>();
	
	public static void initializeObject(Integer objectID, MongoDB_Object lObject) {
		INITIALIZED_OBJECTS.put(objectID, lObject);
	}
	
	/*
	 * FOR TEST
	 */
	public static void clearInitializedLearningObjects() {
		INITIALIZED_OBJECTS.clear();
	}
	
	/*
	 * FOR TEST
	 */
	public static int getSizeOfInitializedLearningObjects() {
		return INITIALIZED_OBJECTS.size();
	}
	
	// For test
	public static List<Integer> getAllLearningObjectIDs() {
		DBCollection collection = MongoDB_Connector.connectToObjectCollection();
		
		List<Integer> objectIDs = new ArrayList<Integer>();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		select.put("_id", 1);
		
		DBCursor cursor = collection.find(idQuery, select);
		
		int objectID;
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			objectID = (Integer) dbObj.get("_id");
			objectIDs.add(objectID);
		}
		
		return objectIDs;
	}
	
	public static LA_Object getLearningObjectByID(Integer objectID) {
		MongoDB_Object lObject = INITIALIZED_OBJECTS.get(objectID);
		
		// Learning object is not yet initialized
		if (lObject == null) {
			DBCollection collection = MongoDB_Connector.connectToObjectCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", objectID);
			
			DBCursor cursor = collection.find(whereQuery).limit(1);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				lObject = new MongoDB_Object(dbObj);
			}
		}
		
		return lObject;
	}
	
	public static List<LA_Object> getLearningObjectsByIDList(List<Integer> objectIDs) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		List<Integer> tmpIDs = new ArrayList<Integer>();
		
		MongoDB_Object lObject;
		
		// Load initialized Learning objects
		for (int aID : objectIDs) {
			lObject = INITIALIZED_OBJECTS.get(aID); 
			if (lObject != null) {
				learningObjects.add(lObject);
			}
			else {
				tmpIDs.add(aID);
			}
		}
		
		// load Learning objects from database which are not initialized
		DBCollection collection = MongoDB_Connector.connectToObjectCollection();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", new BasicDBObject("$in", tmpIDs));
		
		DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			lObject = new MongoDB_Object(dbObj);
			learningObjects.add(lObject);
		}
		
		return learningObjects;
	}
	
	public static LA_Object getLearningObjectParent(int objectID) {
		MongoDB_Object parent = null;
		DBCollection collection = MongoDB_Connector.connectToObjectCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		whereQuery.put("_id", objectID);
		select.put("parent", 1);
		
		DBCursor cursor = collection.find(whereQuery).limit(1);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			parent = createLearningObject(dbObj);
		}
		
		return parent;
	}
	
	// TODO: get children tree... wie bei context implementieren 
	public static List<LA_Object> getChildrenByParentID(Integer objectID) {
		List<LA_Object> childrenList = new ArrayList<LA_Object>();
		
		DBCollection collection = MongoDB_Connector.connectToObjectCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("parent", objectID);
		
		DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()) {

			DBObject dbObj = cursor.next();
			MongoDB_Object lObject = createLearningObject(dbObj);
			
			childrenList.add(lObject);
		}
	
		return childrenList;
	}
	
	/**
	 * Returns recursively all children and their subsequent children.  
	 * @param contextID
	 * @param children
	 * @return
	 */
	protected static List<MongoDB_Object> getChildrenTreeOfLearningObject(Integer objID, 
			List<MongoDB_Object> children) {
	   for( MongoDB_Object child : getChildrenOfLearningObject(objID) )
	   {
		   children.add(child);
		   getChildrenTreeOfLearningObject(child.getID(), children);
	   }
	   return children;
	}
	
	/**
	 * Returns a list of Learning objects which refer to the given object id as parent.
	 * @param contextID
	 * @return
	 */
	private static List<MongoDB_Object> getChildrenOfLearningObject(Integer contextID) {
		List<MongoDB_Object> objChildren = new ArrayList<MongoDB_Object>();
		
		// find all courses
		DBCollection contextCollection = MongoDB_Connector.connectToObjectCollection();
		BasicDBObject parentNullQuery = new BasicDBObject();
		parentNullQuery.put("parent", contextID);
		
		// iterate children
		DBCursor cursor = contextCollection.find(parentNullQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			MongoDB_Object child = createLearningObject(obj);

			objChildren.add(child);
		}
	
		return objChildren;
	}
	
	public static MongoDB_Object getObjectByDescriptor(String descriptor) {
		
		for (MongoDB_Object lObject : INITIALIZED_OBJECTS.values()) {
			if (lObject.equals(descriptor)) {
				return lObject;
			}
		}
		
		return null;
	}
	
	private static MongoDB_Object createLearningObject(DBObject dbObject) {
		Integer objectID = (Integer) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Object obj = INITIALIZED_OBJECTS.get(objectID);
		if (obj == null) {
			obj = new MongoDB_Object(dbObject);
			INITIALIZED_OBJECTS.put(obj.getID(), obj);
		}
		
		return obj;
	}
}
