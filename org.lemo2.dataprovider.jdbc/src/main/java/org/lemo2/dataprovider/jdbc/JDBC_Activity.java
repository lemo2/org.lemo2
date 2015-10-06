package org.lemo2.dataprovider.jdbc;

import org.lemo2.dataprovider.api.*;

import java.util.*;

public class JDBC_Activity implements LA_Activity {
	
	/**
	 * all instantiated learning activities, referenced by database ID
	 */
	static Map<Long,JDBC_Activity> ACTIVITIES = new HashMap<Long,JDBC_Activity>();
		
	long _time;
	String _action = null;
	String _info = null;
	LA_Activity _reference = null;	
	Map<String,String> _extAttr = null;
	LA_Context _context = null;
	LA_Person _person = null;
	LA_Object _object = null;
	
	public JDBC_Activity(Long id, long time, String action, String info,
			Long activityId, Long contextId, Long personId, Long objectId) {
		ACTIVITIES.put(id, this);
		_time = time;
		_action = action;
		_info = info;
		if ( activityId != null ) _reference = JDBC_Activity.findById(activityId);
		if ( contextId != null ) _context = JDBC_Context.findById(contextId);
		if ( personId != null ) _person = JDBC_Person.findById(personId);
		if ( objectId != null ) _object = JDBC_Object.findById(objectId);
	}
	
	public Set<String> getExtNames() {
		if ( _extAttr == null ) return null;
		return _extAttr.keySet();
	}
	
	public String getExtValue(String name) {
		if ( _extAttr == null ) return null;
		return _extAttr.get(name);
	}
		
	public long getTime() {
		return _time;
	}
	
	public String getAction() {
		return _action;
	}
	
	public String getInfo() {
		return _info;
	}
	
	public LA_Activity getReference() {
		return _reference;
	}
	
	public LA_Person getPerson() {
		return _person;
	}
	
	public LA_Object getObject() {
		return _object;
	}
	
	static JDBC_Activity findById(Long id) {
		return ACTIVITIES.get(id);
	}
	
}
