package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Activity;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4j_PersonDataProvider {

	/**
	 * Returns all learning activities of the given person
	 * @param personID
	 * @return
	 */
	public static List<LA_Activity> getActivitiesOfPerson(String personID) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(person:Person) " +
				"WHERE person.personID = '" + personID + "' " + 
				"WITH person " +
				"MATCH (person)-[:DOES]->(activity) " +
				"RETURN activity.activityID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String activityID = record.get("activity.activityID").asString();
			LA_Activity activity = new Neo4j_Activity(activityID);
			activities.add(activity);
		}
		
		return activities;
	}
	
	/**
	 * Returns all learning contexts which the given person is participating in
	 * @param personID
	 * @return
	 */
	public static List<LA_Context> getContextsOfPerson(String personID) {
		List<LA_Context> contexts = new ArrayList<LA_Context>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
	
		String statement = "MATCH(person:Person) " +
				"WHERE person.personID = '" + personID + "' " + 
				"WITH person " +
				"MATCH (person)-[:PARTICIPATES_IN]->(context:LearningContext) " +
				"RETURN context.contextID";
		
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String contextID = record.get("context.contextID").asString();
			
			LA_Context context = new Neo4j_Context(contextID);
			contexts.add(context);
		}
		
		return contexts;
	}
}
