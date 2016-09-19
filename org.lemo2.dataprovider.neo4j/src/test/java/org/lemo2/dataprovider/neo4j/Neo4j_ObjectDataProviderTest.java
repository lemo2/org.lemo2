package org.lemo2.dataprovider.neo4j;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Object;

public class Neo4j_ObjectDataProviderTest {

	String lObjectID = "";
	
	@Test
	public void getTypeOfLearningObject() {
		
	}
	
	@Test
	public void getLearningObjectsOfGivenType_TypeExists() {
		String type = "Questions";
		List<LA_Object> lObjects;
		
		lObjects = Neo4j_ObjectDataProvider.getLearningObjectsOfGivenType(type);
		
		assertEquals(682, lObjects.size());
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
		
	}
	
	@Test
	public void getAllChildrenOfLearningObject() {
		
	}

}
