package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Object;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Person;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;

public class Neo4j_ContextDataProvider {
	
	public static Neo4j_Context getParentOfContext(String contextID) {
		Neo4j_Context parent = null;
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH context " +
				"MATCH (parent:LearningContext)-[r:PARENT_OF]->(context) " +
				"RETURN parent.contextID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String parentContextID = record.get("parent.contextID").asString();
			
			parent = new Neo4j_Context(parentContextID);
		}
		
		return parent;
	}
	
	public static List<LA_Context> getChildrenOfContext(String contextID) {
		List<LA_Context> children = new ArrayList<LA_Context>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (parent:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH parent " +
				"MATCH (parent)-[r:PARENT_OF]->(child) " +
				"RETURN child.contextID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String parentContextID = record.get("child.contextID").toString();
			children.add(new Neo4j_Context(parentContextID));
		}
		
		return children;
	}
	
	public static List<LA_Context> getAllChildrenOfContext(String contextID) {
		List<LA_Context> children = new ArrayList<LA_Context>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (parent:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH parent " +
				"MATCH (parent)-[r:PARENT_OF*1..]->(child) " +
				"RETURN child.contextID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String parentContextID = record.get("child.contextID").toString();
			children.add(new Neo4j_Context(parentContextID));
		}
		
		return children;
	}
	
	public static List<LA_Person> getInstructorsOfContext(String contextID) {
		List<LA_Person> instructors = new ArrayList<LA_Person>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (parent:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH parent " +
				"MATCH (person)-[r:PARTICIPATES_IN {role:'instructor'}]->(parent) " +
				"RETURN person.personID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String personID = record.get("person.personID").asString();
			instructors.add(new Neo4j_Person(personID));
		}
		
		return instructors;
	}
	
	public static List<LA_Person> getStudentsOfContext(String contextID) {
		List<LA_Person> students = new ArrayList<LA_Person>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (parent:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH parent " +
				"MATCH (person)-[r:PARTICIPATES_IN {role:'student'}]->(parent) " +
				"RETURN person.personID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String personID = record.get("person.personID").asString();
			students.add(new Neo4j_Person(personID));
		}
		
		return students;
	}
	
	public static List<LA_Person> getAllStudentsOfContext(String contextID) {
		List<LA_Person> students = new ArrayList<LA_Person>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String personID = record.get("person.personID").asString();
			students.add(new Neo4j_Person(personID));
		}
		
		return students;
	}
	
	public static List<LA_Object> getLearningObjectsOfContext(String contextID) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "MATCH (context:LearningContext {contextID:'" + contextID + "'}) " +
				"WITH context " +
				"MATCH (context)-[r:HAS_OBJECT]->(object:LearningObject) " +
				"RETURN object.objectID";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String objectID = record.get("object.objectID").asString();
			learningObjects.add(new Neo4j_Object(objectID));
		}
		
		return learningObjects;
	}
	
	public static List<LA_Object> getAllLearningObjectsOfContext(String contextID) {
		List<LA_Object> learningObjects = new ArrayList<LA_Object>();
		
		Driver driver = Neo4j_Connector.getDriver();
		Session session = driver.session();
		
		String statement = "";
		StatementResult result = session.run(statement);
		
		while(result.hasNext()) {
			Record record = result.next();
			String objectID = record.get("object.objectID").asString();
			learningObjects.add(new Neo4j_Object(objectID));
		}
		
		return learningObjects;
	}
}
