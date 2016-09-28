package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Activity;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Object;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4j_ObjectDataProvider {

	/**
	 * Return the learning object with the given ID.
	 * @param objectID
	 * @return
	 */
	public static LA_Object getLearningObjectByID(String objectID) {
		LA_Object lObject = null;
		
		if (objectID == null || objectID.equals("")) {
			return lObject;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(lObject:LearningObject) " +
				"WHERE lObject.objectID = '" + objectID + "' " +
				"RETURN lObject.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			lObject = new Neo4j_Object(objectID);
		}
		
		driver.close();
		session.close();
		
		return lObject;
	}
	
	public static String getTypeOfLearningObject(String objectID) {
		String type = "";
		
		if (objectID == null || objectID.equals("")) {
			return type;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(lObject:LearningObject) " +
				"WHERE lObject.objectID =  '" + objectID + "' " +
				"RETURN labels(lObject)";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			List<Object> types = record.get("labels(lObject)").asList();
			for (Object t : types) {
				if (!t.equals("LearningObject")) {
					type = t.toString();
				}
			}
		}
		
		driver.close();
		session.close();
		
		return type;
	}
	
	public static List<LA_Object> getLearningObjectsOfGivenType(String type) {
		List<LA_Object> lObjects = new ArrayList<LA_Object>();
		
		if (type == null || type.equals("")) {
			return lObjects;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(lObject:" + type + ") " +
				"RETURN lObject.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String oID = record.get("lObject.objectID").asString();
			
			LA_Object lObject = new Neo4j_Object(oID);
			lObjects.add(lObject);
		}
		
		driver.close();
		session.close();
		
		return lObjects;
	}
	
	public static LA_Object getParentOfLearningObject(String objectID) {
		Neo4j_Object parent = null;
		
		if (objectID == null || objectID.equals("")) {
			return parent;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (lObject:LearningObject) " +
				"WHERE lObject.objectID = '" + objectID + "' " +
				"WITH lObject " +
				"MATCH (parent:LearningObject)-[r:PARENT_OF]->(lObject) " +
				"RETURN parent.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String parentObjectID = record.get("parent.objectID").asString();
			
			parent = new Neo4j_Object(parentObjectID);
		}
		
		driver.close();
		session.close();
		
		return parent;
	}
	
	/**
	 * Returns the children objects of the given learning object.
	 * @param objectID
	 * @return
	 */
	public static List<LA_Object> getChildrenOfLearningObject(String objectID) {
		List<LA_Object> lObjects = new ArrayList<LA_Object>();
		
		if (objectID == null || objectID.equals("")) {
			return lObjects;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(lObject:LearningObject)-[:PARENT_OF]->(children) " +
				"WHERE lObject.objectID = '" + objectID + "' " + 
				"RETURN children.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String oID = record.get("children.objectID").asString();
			
			LA_Object lObject = new Neo4j_Object(oID);
			lObjects.add(lObject);
		}
		
		driver.close();
		session.close();
		
		return lObjects;
	}
	
	/**
	 * Returns all the children objects (children of the children...) of the given learning object.
	 * @param objectID
	 * @return
	 */
	public static List<LA_Object> getAllChildrenOfLearningObject(String objectID) {
		List<LA_Object> lObjects = new ArrayList<LA_Object>();
		
		if (objectID == null || objectID.equals("")) {
			return lObjects;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(lObject:LearningObject)-[:PARENT_OF*1..]->(children) " +
				"WHERE lObject.objectID = '" + objectID + "' " + 
				"RETURN children.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String oID = record.get("children.objectID").asString();
			
			LA_Object lObject = new Neo4j_Object(oID);
			lObjects.add(lObject);
		}
		
		driver.close();
		session.close();
		
		return lObjects;
	}
	
	public static List<LA_Activity> getActivitiesOfLearningObject(String objectID) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		if (objectID == null || objectID.equals("")) {
			return activities;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH(activity:LearningActivity)-[:USES]->(lObject:LearningObject) " +
				"WHERE lObject.objectID = '" + objectID + "' " + 
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			
			LA_Activity activity = new Neo4j_Activity(activityID);
			activities.add(activity);
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
}
