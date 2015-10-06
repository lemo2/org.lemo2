package org.lemo2.dataprovider.jdbc;

import org.lemo2.dataprovider.api.*;
import java.util.*;

public class JDBC_Context implements LA_Context {
	
	/**
	 * all instantiated learning contexts, referenced by database ID
	 */
	static Map<Long,JDBC_Context> CONTEXTS = new HashMap<Long,JDBC_Context>();
	
	JDBC_DataProvider _dp;
	Long _id;
	
	String _name;
	String _descriptor;
	LA_Context _parent = null;
	List<LA_Context> _children = null;
	Map<String,String> _extAttr = null;
	
	List<LA_Person> _students = null;
	List<LA_Person> _instructors = null;
	List<LA_Object> _objects = null;
	List<LA_Activity> _activities = null;
	
	boolean _studentsInitiated = false;
	boolean _instructorsInitiated = false;
	boolean _objectsInitiated = false;
	boolean _activityInitiated = false;
	
	public JDBC_Context(Long id, String name, JDBC_DataProvider dp) {
		CONTEXTS.put(id, this);
		_id = id;
		_name = name;
		_dp = dp;
		_descriptor = Integer.toString(hashCode());
	}
	
	public Set<String> getExtNames() {
		if ( _extAttr == null ) return null;
		return _extAttr.keySet();
	}
	
	public String getExtValue(String name) {
		if ( _extAttr == null ) return null;
		return _extAttr.get(name);
	}
	
	public String getName() {
		return _name;
	}
	
	public String getDescriptor() {
		return _descriptor;
	}
	
	public LA_Context getParent() {
		return _parent;
	}
	
	public List<LA_Context> getChildren() {
		return _children;
	}
	
	public List<LA_Person> getStudents() {
		if ( ! _studentsInitiated ) {
			_studentsInitiated = true;
			_students = _dp.initStudents(_id);
		}
		return _students;
	}
			
	public List<LA_Person> getInstructors() {
		if ( ! _instructorsInitiated ) {
			_instructorsInitiated = true;
			_instructors = _dp.initInstructors(_id);
		}
		return _instructors;
	}
	
	public List<LA_Object> getObjects() {
		if ( ! _objectsInitiated ) {
			_objectsInitiated = true;
			_objects = _dp.initObjects(_id);
		}
		return _objects;
	}
			
	public List<LA_Activity> getActivities() {
		if ( ! _activityInitiated ) {
			getStudents();
			getInstructors();
			getObjects();
			_activityInitiated = true;
			_activities = _dp.initActivities(_id);
		}
		return _activities;
	}
	
	static JDBC_Context findById(Long id) {
		return CONTEXTS.get(id);
	}
	
	static JDBC_Context findByDescriptor(String descriptor) {
		for ( JDBC_Context context : CONTEXTS.values() ) {
			if ( descriptor.equals(context.getDescriptor()) ) {
				return context;
			}
		}
		return null;
	}
	
}
	
