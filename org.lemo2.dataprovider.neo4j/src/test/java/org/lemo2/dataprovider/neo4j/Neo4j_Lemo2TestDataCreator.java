package org.lemo2.dataprovider.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class Neo4j_Lemo2TestDataCreator {

	int numberOfContexts = 6;
	int numberOfObjects = 5;
	int numberOfStudents = 15;
	int numberOfInstructors = 2;
	int numberOfActivities = 50;
	
	public void removeTestdata() {
		String statement = "MATCH (n) DETACH DELETE n";
		
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run(statement);
		session.close();
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
		
		statements.add("CREATE (o:LearningObject:Exam {objectID:'TestObject_1'})");
		statements.add("CREATE (o:LearningObject:Question {objectID:'TestObject_2'})");
		statements.add("CREATE (o:LearningObject:Question {objectID:'TestObject_3'})");
		statements.add("CREATE (o:LearningObject:Question {objectID:'TestObject_4'})");
		statements.add("CREATE (o:LearningObject:Video {objectID:'TestObject_5'})");
		
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
		createLearningActivityRelationships();
	}
	
	private void createLearningContextRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("CREATE (:LearningContext {contextID: 'TestContext_1'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_2'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_1'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_3'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_3'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_4'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_4'})-[:PARENT_OF]->(:LearningContext {contextID:'TestContext_5'})");
		
		session.run("CREATE (:LearningContext {contextID: 'TestContext_2'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_1'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_2'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_1'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_3'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_3'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_5'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_4'})");
		session.run("CREATE (:LearningContext {contextID: 'TestContext_5'})-[:HAS_OBJECT]->(:LearningObject {contextID:'TestObject_5'})");
		
		session.close();
	}
	
	private void createLearningObjectRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("CREATE (:LearningObject {contextID: 'TestObject_1'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_2'})");
		session.run("CREATE (:LearningObject {contextID: 'TestObject_2'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_3'})");
		session.run("CREATE (:LearningObject {contextID: 'TestObject_4'})-[:PARENT_OF]->(:LearningObject {contextID:'TestObject_5'})");
		
		session.close();
	}
	
	private void createPersonRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		session.run("CREATE (:Person {personID: 'Person_Instructor_1'})-[:PARTICIPATES_IN {role:'Instructor'}]->"+
				"(:LearningContext {contextID:'TestContext_1'})");
		session.run("CREATE (:LearningContext {contextID:'TestContext_1'})-[:HAS_PERSON {role:'Instructor'}]->"+
				"(:Person {personID: 'Person_Instructor_1'})");
		
		session.run("CREATE (:Person {personID: 'Person_Instructor_2'})-[:PARTICIPATES_IN {role:'Instructor'}]->"+
				"(:LearningContext {contextID:'TestContext_4'})");
		session.run("CREATE (:LearningContext {contextID:'TestContext_4'})-[:HAS_PERSON {role:'Instructor'}]->"+
				"(:Person {personID: 'Person_Instructor_2'})");
		
		for (int i = 1; i <= 10; i++) {
			session.run("CREATE (:Person {personID: 'Person_Student_" + i + "'})-[:PARTICIPATES_IN {role:'Student'}]->"+
					"(:LearningContext {contextID:'TestContext_1'})");
			session.run("CREATE (:LearningContext {contextID:'TestContext_1'})-[:HAS_PERSON {role:'Student'}]->"+
					"(:Person {personID: 'Person_Student_" + i + "'})");
		}
		
		for (int i = 11; i <= 15; i++) {
			session.run("CREATE (:Person {personID: 'Person_Student_" + i + "'})-[:PARTICIPATES_IN {role:'Student'}]->"+
					"(:LearningContext {contextID:'TestContext_4'})");
			session.run("CREATE (:LearningContext {contextID:'TestContext_4'})-[:HAS_PERSON {role:'Student'}]->"+
					"(:Person {personID: 'Person_Student_" + i + "'})");
		}
		
		session.close();
	}
	
	private void createLearningActivityRelationships() {
		Driver driver = getDriverForTestDatabase();
		Session session = driver.session();
		
		// HAS_ACTIVITY
		for (int i = 1; i <= 10; i++) {
			session.run("CREATE (:LearningContext {contextID: 'TestContext_2'})-[:HAS_ACTIVITY]->" + 
					"(:LearningActivity {activityID:'Activity_" + i + "'})");
		}
		for (int i = 11; i <= 25; i++) {
			session.run("CREATE (:LearningContext {contextID: 'TestContext_3'})-[:HAS_ACTIVITY]->" + 
					"(:LearningActivity {activityID:'Activity_" + i + "'})");
		}
		for (int i = 26; i <= numberOfActivities; i++) {
			session.run("CREATE (:LearningContext {contextID: 'TestContext_5'})-[:HAS_ACTIVITY]->" + 
					"(:LearningActivity {activityID:'Activity_" + i + "'})");
		}
		
		// USES
		for (int i = 6; i <= 10; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:USE]->" + 
					"(:LearningObject {objectID: 'TestObject_2'})");
		}
		for (int i = 11; i <= 20; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:USE]->" + 
					"(:LearningObject {objectID: 'TestObject_3'})");
		}
		for (int i = 26; i <= 40; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:USE]->" + 
					"(:LearningObject {objectID: 'TestObject_4'})");
		}
		for (int i = 41; i <= numberOfActivities; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:USE]->" + 
					"(:LearningObject {objectID: 'TestObject_5'})");
		}
		
		// REFERENCES
		for (int i = 2; i <= 6; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_1'})");
		}
		for (int i = 7; i <= 10; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_6'})");
		}
		
		for (int i = 12; i <= 19; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_11'})");
		}
		for (int i = 21; i <= 25; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_20'})");
		}
		
		for (int i = 27; i <= 30; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_26'})");
		}
		for (int i = 31; i <= 45; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_30'})");
		}
		for (int i = 46; i <= numberOfActivities; i++) {
			session.run("CREATE (:LearningActivity {activityID:'Activity_" + i + "'})-[:REFERENCES]->" + 
					"(:LearningActivity {activityID:'Activity_45'})");
		}
		
		session.close();
	}
	
	private Driver getDriverForTestDatabase() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "test" ) );
		return driver;
	}
}
