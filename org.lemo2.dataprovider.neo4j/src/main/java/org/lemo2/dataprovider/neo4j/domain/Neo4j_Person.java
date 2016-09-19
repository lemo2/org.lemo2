package org.lemo2.dataprovider.neo4j.domain;

import java.util.Set;

import org.lemo2.dataprovider.api.LA_Person;

public class Neo4j_Person implements LA_Person {

	private String personID;
	
	public Neo4j_Person(String personID) {
		this.personID = personID;
	}
	
	public String getPersonID() {
		return this.personID;
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
}
