package org.lemo2.dataprovider.neo4j;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.domain.Neo4j_Context;

public class Neo4j_ContextDataProviderTest {
	
	@Test
	public void getParentOfContext_parentExists() {
		String contextID = "14";
		Neo4j_Context parent = (Neo4j_Context) Neo4j_ContextDataProvider.getParentOfContext(contextID);
		String cID = parent.getContextID();
		
		assertEquals("1", parent.getContextID());
	}
	
	@Test
	public void getChildrenOfContext_childrenExists() {
		String contextID = "1";
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(contextID);
		
		assertEquals(17, children.size());
	}
	
	@Test
	public void getChildrenOfContext_noChildrenExists() {
		String contextID = "TEST_ID";
		List<LA_Context> children = Neo4j_ContextDataProvider.getChildrenOfContext(contextID);
		
		assertEquals(0, children.size());
	}
	
	@Test
	public void getAllChildrenOfContext() {
		String contextID = "2";
		List<LA_Context> children = Neo4j_ContextDataProvider.getAllChildrenOfContext(contextID);
		
		assertEquals(64, children.size());
	}
	
	@Test
	public void getStudentsOfContext_studentsExists() {
		String contextID = "1";
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(contextID);
		
		assertEquals(6834, students.size());
	}
	
	@Test
	public void getStudentsOfContext_noStudentsExists() {
		String contextID = "TEST_ID";
		List<LA_Person> students = Neo4j_ContextDataProvider.getStudentsOfContext(contextID);
		
		assertEquals(0, students.size());
	}
	
	@Test
	public void getStudentsOfContext_instructorsExists() {
		String contextID = "1";
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(contextID);
		
		assertEquals(4, students.size());
	}
	
	@Test
	public void getStudentsOfContext_noInstructorsExists() {
		String contextID = "TEST_ID";
		List<LA_Person> students = Neo4j_ContextDataProvider.getInstructorsOfContext(contextID);
		
		assertEquals(0, students.size());
	}
}
