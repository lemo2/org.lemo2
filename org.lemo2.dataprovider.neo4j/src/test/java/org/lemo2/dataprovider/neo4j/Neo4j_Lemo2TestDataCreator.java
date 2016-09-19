package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class Neo4j_Lemo2TestDataCreator {

	int numberOfContexts = 5;
	int numberOfObjects = 5;
	int numberOfStudents = 15;
	int numberOfInstructors = 2;
	int numberOfActivities = 50;
	
	public void createTestDatabase() {
		
	}
	
	public void removeTestDatabase() {
		
	}
	
	public void startNeo4j(String databaseName) {
		
	}
	
	public void createTestdata() {
		createNodes();
		createRelationships();
	}
	
	private void createNodes() {
		createLearningContexts();
		createLearningObjects();
		createPersons();
		createLearningActivities();
	}
	
	private void createLearningContexts() {
		
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		List<String> statements = new ArrayList<String>();
		
		for (int i = 1; i <= numberOfContexts; i++) {
			statements.add("CREATE (c:LearningContext {contextID:'TestContext_" + i + "'})");
		}
		
		for (int i = 0; i < statements.size(); i++) {
			session.run(statements.get(i));
		}
		
		session.close();
	}
	
	private void createLearningObjects() {
		
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		List<String> statements = new ArrayList<String>();
		
		for (int i = 1; i <= numberOfObjects; i++) {
			statements.add("CREATE (o:LearningObject {objectID:'TestObject_" + i + "'})");
		}
		
		for (int i = 0; i < statements.size(); i++) {
			session.run(statements.get(i));
		}
		
		session.close();
	}
	
	private void createPersons() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		List<String> statements = new ArrayList<String>();
		
		for (int i = 1; i <= numberOfStudents; i++) {
			statements.add("CREATE (p:Person {personID:'Person_Student_" + i + "'})");
		}
		
		for (int i = 1; i <= numberOfInstructors; i++) {
			statements.add("CREATE (p:Person {personID:'Person_Instructor_" + i + "'})");
		}
		
		for (int i = 0; i < statements.size(); i++) {
			session.run(statements.get(i));
		}
		
		session.close();
	}
	
	private void createLearningActivities() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		List<String> statements = new ArrayList<String>();
		
		for (int i = 1; i <= numberOfActivities; i++) {
			statements.add("CREATE (a:LearningActivity {activityID:'Activity_" + i + "'})");
		}
		
		for (int i = 0; i < statements.size(); i++) {
			session.run(statements.get(i));
		}
		
		session.close();
	}
	
	private void createRelationships() {
		createLearningContextRelationships();
		createLearningObjectRelationships();
		createPersonRelationships();
	}
	
	private void createLearningContextRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("(:LearningContext {contextID: 'TestContext_1'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_2'})");
		session.run("(:LearningContext {contextID: 'TestContext_1'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_3'})");
		session.run("(:LearningContext {contextID: 'TestContext_4'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_5'})");
		
		session.run("(:LearningContext {contextID: 'TestContext_2'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_1'})");
		session.run("(:LearningContext {contextID: 'TestContext_2'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("(:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_1'})");
		session.run("(:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("(:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_3'})");
		session.run("(:LearningContext {contextID: 'TestContext_5'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_4'})");
		session.run("(:LearningContext {contextID: 'TestContext_5'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_5'})");
		
		session.close();
	}
	
	private void createLearningObjectRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("(:LearningObject {contextID: 'TestObject_1'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("(:LearningObject {contextID: 'TestObject_1'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_3'})");
		session.run("(:LearningObject {contextID: 'TestObject_4'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_5'})");
		
		session.close();
	}
	
	private void createPersonRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("(:Person {personID: 'Person_Instructor_1'})-[:PARTICIPATES_IN {role:'instructor'}]->"+
				"(:LearningContext {contextID:'TestContext_1'})");
		session.run("(:Person {personID: 'Person_Instructor_2'})-[:PARTICIPATES_IN {role:'instructor'}]->"+
				"(:LearningContext {contextID:'TestContext_4'})");
		
		for (int i = 1; i <= 10; i++) {
			session.run("(:Person {personID: 'Person_Student_" + i + "'})-[:PARTICIPATES_IN {role:'student'}]->"+
					"(:LearningContext {contextID:'TestContext_1'})");
		}
		
		for (int i = 11; i <= 15; i++) {
			session.run("(:Person {personID: 'Person_Student_" + i + "'})-[:PARTICIPATES_IN {role:'student'}]->"+
					"(:LearningContext {contextID:'TestContext_4'})");
		}
		
		session.close();
	}
	
	private void createLearningActivityRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		// USES
		
		// REFERENCES
		
		
		session.close();
	}
	
	private Driver getDriverForTestDatabase() {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "JUnitTestPW" ) );
		
		return driver;
	}
}
