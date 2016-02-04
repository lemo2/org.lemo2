package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Object;
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
	
	public static LA_Object getLearningObjectByID(Integer objectID) {
		LA_Object lObject = INITIALIZED_OBJECTS.get(objectID);
		
		// Learning object is not yet initialized
		if (lObject == null) {
			DBCollection collection = MongoDB_Connector.connectToObjectCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", objectID);
			
			DBCursor cursor = collection.find(whereQuery);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				lObject = new MongoDB_Object(dbObj);
				return lObject;
			}
		}
		
		return lObject;
	}
	
	public static List<LA_Object> getLearningObjectsByIDList(List<Integer> objectIDs) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		List<Integer> tmpIDs = new ArrayList<Integer>();
		
		LA_Object lObject;
		
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
	
	public static List<LA_Object> getChildrenByParentID(Integer objectID) {
		List<LA_Object> childrenList = new ArrayList<LA_Object>();
		
		DBCollection collection = MongoDB_Connector.connectToObjectCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("parent", objectID);
		
		DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			Integer childrenID = (Integer) dbObj.get("_id");
			
			// load / create children domain object
			LA_Object lObject = INITIALIZED_OBJECTS.get(childrenID);
			
			if (lObject == null) {
				lObject = new MongoDB_Object(dbObj);
			}
			
			childrenList.add(lObject);
		}
		
		return childrenList;
	}
	
	public static LA_Object getObjectByDescriptor(String descriptor) {
		
		for (LA_Object lObject : INITIALIZED_OBJECTS.values()) {
			if (lObject.equals(descriptor)) {
				return lObject;
			}
		}
		
		return null;
	}
}
