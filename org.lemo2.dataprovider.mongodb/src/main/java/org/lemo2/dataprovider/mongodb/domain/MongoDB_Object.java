package org.lemo2.dataprovider.mongodb.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Object;
import org.lemo2.dataprovider.mongodb.MongoDB_ObjectDataProvider;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class MongoDB_Object implements LA_Object {
	
	private int objectID;
	private String descriptor;
	private String name;
	private String type;
	private Map<String, String> extAttributes;
	private LA_Object parent;
	private List<LA_Object> children;
	
	public MongoDB_Object(DBObject dbObject) {
		this.descriptor = Integer.toString(hashCode());
		this.objectID = (int) dbObject.get("_id");
		this.name = (String) dbObject.get("name");
		this.type = (String) dbObject.get("type");
		/*
		extractExtensions(dbObject);
		extractParent(dbObject);
		extractChildren(dbObject);
		*/
		initialize();
	}
	
	private void initialize() {
		MongoDB_ObjectDataProvider.initializeObject(objectID, this);
	}
	
	/**
	 * Extract parent object
	 * @param dbObject
	 */
	private void extractParent(DBObject dbObject) {
		Integer parentID = (Integer) dbObject.get("parent");
		
		LA_Object parent = MongoDB_ObjectDataProvider.getLearningObjectByID(parentID);
		this.parent = parent;
	}
	
	/**
	 * Extract children objects
	 * @param dbObject
	 */
	private void extractChildren(DBObject dbObject) {
		Integer objectID = (Integer) dbObject.get("_id");
		
		List<LA_Object> children = MongoDB_ObjectDataProvider.getChildrenByParentID(objectID);
		this.children = children;
	}
	
	/**
	 * Extract learning object extensions
	 * @param dbObject
	 */
	private void extractExtensions(DBObject dbObject) {
		extAttributes = new HashMap<String, String>();
		
		BasicDBList extList = (BasicDBList) dbObject.get("extensions");
	   
		if (extList != null) {
		    for (int i = 0; i < extList.size(); i++) {
		    	BSONObject extention = (BSONObject) extList.get(i);
		    	String attr = (String) extention.get("ext_attr");
		    	String value = (String) extention.get("ext_value");
		    	
		    	extAttributes.put(attr, value);
		    }
		}
	}
	
	public int getID() {
		return objectID;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescriptor() {
		return descriptor;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Set<String> extAttributes() {
		if (extAttributes == null) {
			return null;
		}
		else {
			return extAttributes.keySet();
		}
	}

	@Override
	public String getExtAttribute(String attrKey) {
		if (extAttributes == null) {
			return null;
		}
		else {
			return extAttributes.get(attrKey);
		}
	}

	@Override
	public LA_Object getParent() {
		if (parent == null) {
			parent = MongoDB_ObjectDataProvider.getLearningObjectParent(objectID);
		}
		return parent;
	}

	@Override
	public List<LA_Object> getChildren() {
		if (children == null) {
			children = MongoDB_ObjectDataProvider.getChildrenByParentID(objectID);
		}
		return children;
	}
}
