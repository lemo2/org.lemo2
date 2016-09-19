package org.lemo2.dataprovider.neo4j.domain;

import java.util.List;
import java.util.Set;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.Neo4j_ContextDataProvider;

public class Neo4j_Context implements LA_Context {
	
	private String contextID;
	
	public Neo4j_Context(String contextID) {
		this.contextID = contextID;
	}
	
	public String getContextID() {
		return this.contextID;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getDescriptor() {
		return "";
	}

	@Override
	public Set<String> extAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExtAttribute(String attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LA_Context getParent() {
		return Neo4j_ContextDataProvider.getParentOfContext(this.contextID);
	}

	@Override
	public List<LA_Context> getChildren() {
		return Neo4j_ContextDataProvider.getChildrenOfContext(this.contextID);
	}

	@Override
	public List<LA_Object> getObjects() {
		return Neo4j_ContextDataProvider.getLearningObjectsOfContext(this.contextID);
	}

	@Override
	public List<LA_Object> getAllObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Person> getStudents() {
		return Neo4j_ContextDataProvider.getStudentsOfContext(this.contextID);
	}

	@Override
	public List<LA_Person> getAllStudents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Person> getInstructors() {
		return Neo4j_ContextDataProvider.getInstructorsOfContext(this.contextID);
	}

	@Override
	public List<LA_Person> getAllInstructors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getAllActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getActivities(LA_Person person, LA_Object obj,
			long start, long end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LA_Activity> getAllActivities(LA_Person person, LA_Object obj,
			long start, long end) {
		// TODO Auto-generated method stub
		return null;
	}

}
