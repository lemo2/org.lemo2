package org.lemo2.dataprovider.neo4j.performance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.Neo4j_Connector;
import org.lemo2.dataprovider.neo4j.Neo4j_ContextDataProvider;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4j_ContextDataProviderPerformanceTest {
	
	private final String MATHEMATISCH_DENKEN = "1";
	private final String THE_FUTURE_OF_STORYTELLING = "2";
	private final String NON_EXISTING_CONTEXT = "NO_CONTEXT";
	
	@Before
	public void setUp() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "!!12L3m0" ) );
		Neo4j_Connector.setDriver(driver);
	}
	
	@Test
	public void getParentOfContext_parentExists() {
		String contextID = "14";
		Neo4j_Context parent = (Neo4j_Context) Neo4j_ContextDataProvider.getParentOfContext(contextID);
		
		assertEquals(MATHEMATISCH_DENKEN, parent.getContextID());
	}
	
	@Test
	public void getChildrenOfContext_childrenExists() {
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(MATHEMATISCH_DENKEN);
		
		assertEquals(17, children.size());
	}
	
	@Test
	public void getChildrenOfContext_noChildrenExists_ReturnEmptyList() {
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(NON_EXISTING_CONTEXT);
		
		assertEquals(0, children.size());
	}
	
	@Test
	public void getAllChildrenOfContext() {
		List<LA_Context> children = Neo4j_ContextDataProvider.getAllChildrenOfContext(THE_FUTURE_OF_STORYTELLING);
		
		assertEquals(64, children.size());
	}
	
	@Test
	public void getStudentsOfContext_studentsExists() {
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(THE_FUTURE_OF_STORYTELLING);
		
		assertEquals(93049, students.size());
	}
	
	@Test
	public void getAllStudentsOfContext_studentsExists() {
		List<LA_Person> students = Neo4j_ContextDataProvider.getAllStudentsOfContext(MATHEMATISCH_DENKEN);
		
		assertEquals(6834, students.size());
	}
	
	@Test
	public void getStudentsOfContext_noStudentsExists_ReturnEmptyList() {
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(NON_EXISTING_CONTEXT);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void getInstructorsOfContext_instructorsExists() {
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(MATHEMATISCH_DENKEN);
		
		assertEquals(4, students.size());
	}
	
	@Test
	public void getInstructorsOfContext_noInstructorsExists_ReturnEmptyList() {
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(NON_EXISTING_CONTEXT);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void getPersonsWithRoleOfContext_RoleExists() {
		String role = "Student";
		List<LA_Person> persons = Neo4j_ContextDataProvider.getPersonsWithRoleOfContext(MATHEMATISCH_DENKEN, role);
		
		assertEquals(6834, persons.size());
	}
	
	@Test
	public void getPersonsWithRoleOfContext_RoleDoesNotExists_ReturnEmptyList() {
		String role = "NON_EXISTING_ROLE";
		List<LA_Person> persons = Neo4j_ContextDataProvider.getPersonsWithRoleOfContext(MATHEMATISCH_DENKEN, role);
		
		assertEquals(0, persons.size());
	}
	
	@Test
	public void getLearningObjectsOfContext() {
		List<LA_Object> lObjects = Neo4j_ContextDataProvider.getLearningObjectsOfContext(THE_FUTURE_OF_STORYTELLING);
		
		assertEquals(0, lObjects.size());
	}
	
	@Test
	public void getAllLearningObjectsOfContext() {
		List<LA_Object> lObjects = Neo4j_ContextDataProvider.getAllLearningObjectsOfContext(THE_FUTURE_OF_STORYTELLING);
		
		assertEquals(0, lObjects.size());
	}
}
