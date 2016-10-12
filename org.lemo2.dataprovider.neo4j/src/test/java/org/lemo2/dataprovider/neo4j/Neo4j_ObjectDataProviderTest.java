package org.lemo2.dataprovider.neo4j;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Object;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4j_ObjectDataProviderTest {
	
	@Before
	public void setUp() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "test" ) );
		Neo4j_Connector.setDriver(driver);
	}
	
	@Test
	public void getTypeOfLearningObject() {
		String objectID = "TestObject_1";
		
		String type = Neo4j_ObjectDataProvider.getTypeOfLearningObject(objectID);
		
		assertEquals("Exam", type);
	}
	
	@Test
	public void getLearningObjectsOfGivenType_TypeExists() {
		String type = "Question";
		List<LA_Object> lObjects;
		
		lObjects = Neo4j_ObjectDataProvider.getLearningObjectsOfGivenType(type);
		
		assertEquals(3, lObjects.size());
	}
	
	@Test
	public void getLearningObjectsOfGivenType_NoTypeExists() {
		String type = "NO_TYPE";
		List<LA_Object> lObjects;
		
		lObjects = Neo4j_ObjectDataProvider.getLearningObjectsOfGivenType(type);
		
		assertEquals(0, lObjects.size());
	}
	
	@Test
	public void getChildrenOfLearningObject() {
		String objectID = "TestObject_1";
		
		List<LA_Object> children = Neo4j_ObjectDataProvider.getChildrenOfLearningObject(objectID);
		
		assertEquals(1, children.size());
	}
	
	@Test
	public void getAllChildrenOfLearningObject() {
		String objectID = "TestObject_1";
		
		List<LA_Object> children = Neo4j_ObjectDataProvider.getAllChildrenOfLearningObject(objectID);
		
		assertEquals(2, children.size());
	}

}
