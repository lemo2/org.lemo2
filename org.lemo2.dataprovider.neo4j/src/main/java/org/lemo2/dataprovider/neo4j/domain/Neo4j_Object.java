package org.lemo2.dataprovider.neo4j.domain;

import java.util.List;
import java.util.Set;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.neo4j.Neo4j_ObjectDataProvider;

public class Neo4j_Object implements LA_Object {

	private String objectID;
	private String type;
	
	public Neo4j_Object(String objectID) {
		this.objectID = objectID;
	}

	public String getObjectID() {
		return this.objectID;
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
	public String getType() {
		if (this.type == null || this.type.equals("")) {
			this.type = Neo4j_ObjectDataProvider.getTypeOfLearningObject(this.objectID);
		}
		
		return type;
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
	public LA_Object getParent() {
		return Neo4j_ObjectDataProvider.getParentOfLearningObject(this.objectID);
	}

	@Override
	public List<LA_Object> getChildren() {
		return Neo4j_ObjectDataProvider.getChildrenOfLearningObject(this.objectID);
	}
	
	public List<LA_Object> getAllChildrenOfLearningObject() {
		return Neo4j_ObjectDataProvider.getAllChildrenOfLearningObject(this.objectID);
	}
	
	public List<LA_Activity> getActivitiesOfLearningObject() {
		return Neo4j_ObjectDataProvider.getActivitiesOfLearningObject(this.objectID);
	}
}
