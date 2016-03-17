package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context2;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_ContextDataProvider2 {

	private static Map<Integer, MongoDB_Context2> INITIALIZED_CONTEXTS = new HashMap<Integer, MongoDB_Context2>();
	
	public static void initializeContext(Integer contextID, MongoDB_Context2 context) {
		INITIALIZED_CONTEXTS.put(contextID, context);
	}
	
	/*
	 * FOR TEST
	 */
	public static void clearInitializedContexts() {
		INITIALIZED_CONTEXTS.clear();
	}
	
	public static LA_Context getContextByID(Integer contextID) {
		MongoDB_Context2 context = INITIALIZED_CONTEXTS.get(contextID);
		
		// Learning context is not yet initialized
		if (context == null) {
			DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", contextID);
			
			DBCursor cursor = collection.find(whereQuery).limit(1);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				context = new MongoDB_Context2(dbObj);
			}
		}
		
		return context;
	}
	
	/**
	 * Returns all Learning Object IDs of the given Learning Context.
	 * @param contextID
	 * @return
	 */
	public static List<Integer> getContextLearningObjectIDs(Integer contextID) {
		DBCollection collection = MongoDB_Connector.connectToContextActivityCollection();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", contextID);
		DBObject result = collection.findOne(whereQuery);
		
		List<Integer> objectIDs = (List<Integer>) result.get("learningObjects");
		
		return objectIDs;
	}
	
	/**
	 * Returns recursively all children and their subsequent children of the given context.
	 * @param context
	 * @param children
	 * @return
	 */
	public static List<LA_Context> getChildrenTreeOfContext(MongoDB_Context2 context, List<LA_Context> children)
	{
	   for( LA_Context child : getFirstDegreeChildrenOfContext(context.getID()) )
	   {
		   MongoDB_Context2 mChild = (MongoDB_Context2) child;
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
			MongoDB_Context2 child = createContextObject(obj);
			
			contextChildren.add(child);
		}
	
		return contextChildren;
	}
	
	/**
	 * Checks if the given learning context was already initialized. If not 
	 * create a new 'MongoDB_Context' instance.
	 * @param dbObject
	 * @return
	 */
	private static MongoDB_Context2 createContextObject(DBObject dbObject) {
		Integer contextID = (Integer) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Context2 context = INITIALIZED_CONTEXTS.get(contextID);
		if (context == null) {
			context = new MongoDB_Context2(dbObject);
		}
		
		return context;
	}
	
}
