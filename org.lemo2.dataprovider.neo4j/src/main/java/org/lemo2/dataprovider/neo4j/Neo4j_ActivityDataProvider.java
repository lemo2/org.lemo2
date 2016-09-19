package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Activity;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Object;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Person;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4j_ActivityDataProvider {

	public static List<LA_Activity> getAllActivitiesOfContext(LA_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext = (Neo4j_Context) context;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + nContext.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY*2]->(activity:LearningActivity) " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context) {	
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext = (Neo4j_Context) context;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + nContext.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY]->(activity:LearningActivity) " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person) {	
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext = (Neo4j_Context) context;
		Neo4j_Person nPerson = (Neo4j_Person) person;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + nContext.getContextID() + "' " +
				"WITH context " +
				"MATCH (person:Person {personID:'" + nPerson.getPersonID() + "'})" +
				"WITH person " +
				"MATCH (person)-[:DOES]->(activity:LearningActivity)" +
				"RETURN activity.activityID";

		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext = (Neo4j_Context) context;
		Neo4j_Person nPerson = (Neo4j_Person) person;
		Neo4j_Object nObject = (Neo4j_Object) obj;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + nContext.getContextID() + "' " +
				"WITH context " +
				"MATCH (person:Person {personID:'" + nPerson.getPersonID() + "'}) " +
				"WITH person " +
				"MATCH (person)-[:DOES]->(activity:LearningActivity) " +
				"WITH activity " +
				"MATCH (activity)-[:REFERENCES]->(:LearningObject {objectID:'" + nObject.getObjectID() + "'}) " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		return activities;
	}
	
	public static LA_Activity getReferenceOfActivity(String activityID) {
		LA_Activity reference = null;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(activity:LearningActivity) " +
				"WHERE activity.activityID = '" + activityID + "' " + 
				"WITH activity " +
				"MATCH (activity)-[:REFERENCES]->(reference) " +
				"RETURN reference.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String aID = record.get("reference.activityID").asString();
			reference = new Neo4j_Activity(aID);
		}
		
		return reference;
	}
	
	public static LA_Object getLearningObjectOfActivity(String activityID) {
		LA_Object lObject = null;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (activity:LearningActivity) " + 
				"WHERE activity.activityID = '" + activityID + "' " + 
				"WITH activity " +
				"MATCH (activity)-[:USES]->(lObject:LearningObject) " + 
				"RETURN lObject.objectID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String objectID = record.get("lObject.objectID").asString();
			lObject = new Neo4j_Object(objectID);
		}
		
		return lObject;
	}
	
	public static LA_Person getPersonOfActivity(String activityID) {
		LA_Person person = null;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (activity:LearningActivity) " + 
				"WHERE activity.activityID = '" + activityID + "' " + 
				"WITH activity " +
				"MATCH (person:Person)-[:DOES]->(activity) " + 
				"RETURN person.personID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String personID = record.get("person.personID").asString();
			person = new Neo4j_Person(personID);
		}
		
		return person;
	}
}
