package org.lemo2.dataprovider.neo4j.domain;

import java.util.Set;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.neo4j.Neo4j_ActivityDataProvider;

public class Neo4j_Activity implements LA_Activity {

	private String activityID;
	private String action;
	private long time;
	
	public Neo4j_Activity(String activityID) {
		this.activityID = activityID;
	}
	
	public String getActivityID() {
		return this.activityID;
	}
	
	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public String getAction() {
		return this.action;
	}

	@Override
	public String getInfo() {
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
	public LA_Activity getReference() {
		return Neo4j_ActivityDataProvider.getReferenceOfActivity(this.activityID);
	}

	@Override
	public LA_Object getObject() {
		return Neo4j_ActivityDataProvider.getLearningObjectOfActivity(this.activityID);
	}

	@Override
	public LA_Person getPerson() {
		return Neo4j_ActivityDataProvider.getPersonOfActivity(this.activityID);
	}

}
