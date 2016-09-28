package org.lemo2.dataprovider.neo4j;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
	
	private final static Logger LOGGER = Logger.getLogger(Neo4j_ActivityDataProvider.class.getName()); 

	public static List<LA_Activity> getAllActivitiesOfContext(LA_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext;
		
		if (context == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + nContext.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY*1..]->(activity:LearningActivity) " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	public static List<LA_Activity> getAllActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Neo4j_Context nContext;
		Neo4j_Person nPerson;
		Neo4j_Object nObj;
		
		if (context == null || person == null || obj == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
			nPerson = (Neo4j_Person) person;
			nObj = (Neo4j_Object) obj;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (person:Person)-[d:DOES]->(activity:LearningActivity) " +
				"WHERE person.personID = '" + nPerson.getPersonID() + "' " +
				"WITH activity " + 
				"MATCH(lObject:LearningObject)<-[:USES]-(activity)<-[:HAS_ACTIVITY]-(context:LearningContext) " +
				"WHERE lObject.objectID = '" + nObj.getObjectID() + "' " +
				"AND context.contextID = '" + nContext.getContextID() + "' " +
				"WITH activity RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	public static List<LA_Activity> getAllActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj,
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Neo4j_Context nContext;
		Neo4j_Person nPerson;
		Neo4j_Object nObj;
		
		if (context == null || person == null || obj == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
			nPerson = (Neo4j_Person) person;
			nObj = (Neo4j_Object) obj;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (person:Person)-[d:DOES]->(activity:LearningActivity) " +
				"WHERE person.personID = '" + nPerson.getPersonID() + "' " +
				"d.time >= " + start + " AND d.time <= " + end +
				"WITH activity " + 
				"MATCH(lObject:LearningObject)<-[:USES]-(activity)<-[:HAS_ACTIVITY]-(context:LearningContext) " +
				"WHERE lObject.objectID = '" + nObj.getObjectID() + "' " +
				"AND context.contextID = '" + nContext.getContextID() + "' " +
				"WITH activity RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
	
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context) {	
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext;
		
		if (context == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
		}
		
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
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person) {	
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Neo4j_Context nContext;
		Neo4j_Person nPerson;
		
		if (context == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
		}
		
		if (person == null) {
			activities = getActivitiesWildcardPersonAndLearningObjectAndTimerange(nContext);
		}
		else {
			nPerson = (Neo4j_Person) person;
			
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
			
			driver.close();
			session.close();
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Neo4j_Context nContext;
		Neo4j_Object nObject;
		Neo4j_Person nPerson;
		
		if (context == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
		}
		
		if (person == null && obj == null) {
			activities = getActivitiesWildcardPersonAndLearningObjectAndTimerange(nContext);
		}
		else if (person != null && obj == null) {
			nPerson = (Neo4j_Person) person;
			
			activities = getActivitiesWildcardLearningObjectAndTimerange(nContext, nPerson);
		}
		else if (person == null && obj != null) {
			nObject = (Neo4j_Object) obj;
			
			activities = getActivitiesWildcardPersonAndTimerange(nContext, nObject);
		}
		else {
			nPerson = (Neo4j_Person) person;
			nObject = (Neo4j_Object) obj;
			
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
			
			driver.close();
			session.close();
		}
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj,
			Date start, Date end) {
		
		long lStart = start.getTime();
		long lEnd = end.getTime();
		
		List<LA_Activity> activities = getActivitiesOfContext(context, person, obj, lStart, lEnd);
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj,
			Timestamp start, Timestamp end) {
		
		long lStart = start.getTime();
		long lEnd = end.getTime();
		
		List<LA_Activity> activities = getActivitiesOfContext(context, person, obj, lStart, lEnd);
		
		return activities;
	}
	
	public static List<LA_Activity> getActivitiesOfContext(LA_Context context, LA_Person person, LA_Object obj,
			long start, long end) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Neo4j_Context nContext;
		Neo4j_Person nPerson;
		Neo4j_Object nObject;
		
		// choose 
		if (context == null) {
			return activities;
		}
		else {
			nContext = (Neo4j_Context) context;
		}
		
		if (person != null && obj != null) {
			nPerson = (Neo4j_Person) person;
			nObject = (Neo4j_Object) obj;		
			
			activities = getActivitiesNoWildcard(nContext, nPerson, nObject, start, end);
		}
		else if (person != null && obj == null) {
			nPerson = (Neo4j_Person) person;		
			
			activities = getActivitiesWildcardLearningObject(nContext, nPerson, start, end);
		}
		else if (person == null && obj != null) {
			nObject = (Neo4j_Object) obj;
			
			activities = getActivitiesWildcardPerson(nContext, nObject, start, end);
		}
		else {
			activities = getActivitiesWildcardPersonAndLearningObject(nContext, start, end);
		}
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesNoWildcard(Neo4j_Context context, Neo4j_Person person, 
			Neo4j_Object object, long start, long end) {
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext)<-[:PARTICIPATES_IN]-(person:Person) " +
				"WHERE context.contextID = '" + context.getContextID() + "' AND person.personID = " +
				"'" + person.getPersonID() + "' " +
				"WITH context, person " +
				"MATCH (person)-[does:DOES]->" + 
				"(activity:LearningActivity)-[:USES]->(object:LearningObject) " +
				"WHERE object.objectID = '" + object.getObjectID() + "' " +
				buildTimeRangeQuery("does", start, end) +
				"WITH activity " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardLearningObject(Neo4j_Context context, 
			Neo4j_Person person, long start, long end) {
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)<-[:PARTICIPATES_IN]-(person:Person) " +
				"WHERE person.personID = '" + person.getPersonID() + "' " +
				"WITH person " +
				"MATCH (person)-[does:DOES]->(activity:LearningActivity) " +
				buildTimeRangeQuery("does", start, end) +
				"WITH activity " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPerson(Neo4j_Context context, Neo4j_Object object, 
			long start, long end) {
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)<-[:PARTICIPATES_IN]-(person:Person) " +
				"WITH person " +
				"MATCH (person)-[does:DOES]->(activity:LearningActivity) " +
				buildTimeRangeQuery("does", start, end) +
				"WITH activity " +
				"(activity)-[:USES]->(object:LearningObject) " +
				"WHERE object.objectID = '" + object.getObjectID() + "' " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObject(Neo4j_Context context, 
			long start, long end) {
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext)<-[:PARTICIPATES_IN]-(person:Person) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context, person " +
				"MATCH (person)-[does:DOES]->(activity:LearningActivity)-[:USES]->(:LearningObject) " +
				buildTimeRangeQuery("does", start, end) +
				"WITH activity " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndTimerange(Neo4j_Context context, 
			Neo4j_Object object) {
		
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY]->(activity:LearningActivity)-[:USES]-(object:LearningObject) " +
				"WHERE object.objectID = '" + object.getObjectID() + "' " +
				"RETURN activity.activityID";

		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardLearningObjectAndTimerange(Neo4j_Context context, 
			Neo4j_Person person) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY]->(activity:LearningActivity)<-[:DOES]-(person:Person) " +
				"WHERE person.personID = '" + person.getPersonID() + "' " +
				"RETURN activity.activityID";

		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	private static List<LA_Activity> getActivitiesWildcardPersonAndLearningObjectAndTimerange(Neo4j_Context context) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext) " +
				"WHERE context.contextID = '" + context.getContextID() + "' " +
				"WITH context " +
				"MATCH (context)-[r:HAS_ACTIVITY]->(activity:LearningActivity) " +
				"RETURN activity.activityID";

		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			activities.add(new Neo4j_Activity(activityID));
		}
		
		driver.close();
		session.close();
		
		return activities;
	}
	
	/**
	 * Builds and returns a query based on the given values.
	 * @param vName - variable name 
	 * @param start
	 * @param end
	 * @return
	 */
	private static String buildTimeRangeQuery(String vName, long start, long end) {
		String timerange = "";
		
		if (start > 0 && end > 0) {
			return timerange = String.format("WHERE %s.time >= %d AND %s.time <= %d ", vName, start, end);
		}
		else if (start > 0 && end == 0) {
			return timerange = String.format("WHERE %s.time >= %d ", vName, start);
		}
		else if (start == 0 && end > 0) {
			return timerange = String.format("WHERE %s.time <= %d ", vName, end);
		}
		else {
			return timerange;
		}
	}
	
	public static LA_Context getContextOfActivity(String activityID) {
		LA_Context context = null;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(activity:LearningActivity) " +
				"WHERE activity.activityID = '" + activityID + "' " + 
				"WITH activity " +
				"MATCH (context:LearningContext)-[:HAS_ACTIVITY]->(activity) " +
				"RETURN context.contextID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String contextID = record.get("context.contextID").asString();
			context = new Neo4j_Context(contextID);
		}
		
		driver.close();
		session.close();
		
		return context;
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
		
		driver.close();
		session.close();
		
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
		
		driver.close();
		session.close();
		
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
		
		driver.close();
		session.close();
		
		return person;
	}
	
	public static String getActionOfActivity(String activityID) {
		String action = "";
		
		if (activityID == null || activityID.equals("")) {
			return action;
		}
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(activity:LearningActivity) " +
				"WHERE activity.activityID =  '" + activityID + "' " +
				"RETURN labels(activity)";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			List<Object> actions = record.get("labels(activity)").asList();
			for (Object a : actions) {
				if (!a.equals("LearningActivity")) {
					action = a.toString();
				}
			}
		}
		
		driver.close();
		session.close();
		
		return action;
	}
}
