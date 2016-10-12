package org.lemo2.dataprovider.neo4j;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4j_ContextDataProviderTest {
	
	@Before
	public void setUp() {
		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "test" ) );
		Neo4j_Connector.setDriver(driver);
	}
	
	@Test
	public void getParentOfContext_parentExists() {
		String contextID = "TestContext_2";
		Neo4j_Context parent = (Neo4j_Context) Neo4j_ContextDataProvider.getParentOfContext(contextID);
		
		assertEquals("TestContext_1", parent.getContextID());
	}
	
	@Test
	public void getChildrenOfContext_childrenExists() {
		String contextID = "TestContext_1";
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(contextID);
		
		assertEquals(2, children.size());
	}
	
	@Test
	public void getChildrenOfContext_noChildrenExists_ReturnEmptyList() {
		String contextID = "TestContext_5";
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(contextID);
		
		assertEquals(0, children.size());
	}
	
	@Test
	public void getAllChildrenOfContext() {
		String contextID = "TestContext_1";
		List<LA_Context> children = Neo4j_ContextDataProvider.getAllChildrenOfContext(contextID);
		
		assertEquals(5, children.size());
	}
	
	@Test
	public void getStudentsOfContext_studentsExists() {
		String contextID = "1";
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(contextID);
		
		assertEquals(6834, students.size());
	}
	
	@Test
	public void getAllStudentsOfContext_studentsExists() {
		String contextID = "1";
		List<LA_Person> students = Neo4j_ContextDataProvider.getAllStudentsOfContext(contextID);
		
		assertEquals(6834, students.size());
	}
	
	@Test
	public void getStudentsOfContext_noStudentsExists_ReturnEmptyList() {
		String contextID = "TEST_ID";
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(contextID);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void getInstructorsOfContext_instructorsExists() {
		String contextID = "1";
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(contextID);
		
		assertEquals(4, students.size());
	}
	
	@Test
	public void getInstructorsOfContext_noInstructorsExists_ReturnEmptyList() {
		String contextID = "TEST_ID";
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(contextID);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void getPersonsWithRoleOfContext_RoleExists() {
		String contextID = "1";
		String role = "Student";
		List<LA_Person> persons = Neo4j_ContextDataProvider.getPersonsWithRoleOfContext(contextID, role);
		
		assertEquals(6834, persons.size());
	}
	
	@Test
	public void getPersonsWithRoleOfContext_RoleDoesNotExists_ReturnEmptyList() {
		String contextID = "1";
		String role = "NON_EXISTING_ROLE";
		List<LA_Person> persons = Neo4j_ContextDataProvider.getPersonsWithRoleOfContext(contextID, role);
		
		assertEquals(0, persons.size());
	}
}
