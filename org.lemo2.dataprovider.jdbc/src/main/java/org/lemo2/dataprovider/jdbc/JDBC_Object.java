package org.lemo2.dataprovider.jdbc;

import org.lemo2.dataprovider.api.*;
import java.util.*;

public class JDBC_Object implements LA_Object {
	
	/**
	 * all instantiated learning objects, referenced by database ID
	 */
	static Map<Long,JDBC_Object> OBJECTS = new HashMap<Long,JDBC_Object>();
	
	String _name = null;
	String _descriptor;
	String _type = null;
	LA_Object _parent = null;
	List<LA_Object> _children = null;
	Map<String,String> _extAttr = null;
	
	public JDBC_Object(Long id, String name, String type) {
		OBJECTS.put(id, this);
		_name = name;
		_descriptor = Integer.toString(hashCode());
		_type = type;
	}

	public String getName() {
		return _name;
	}
	
	public String getDescriptor() {
		return _descriptor;
	}
	
	public String getType() {
		return _type;
	}
	
	public Set<String> getExtNames() {
		if ( _extAttr == null ) return null;
		return _extAttr.keySet();
	}
	
	public String getExtValue(String name) {
		if ( _extAttr == null ) return null;
		return _extAttr.get(name);
	}
	
	public LA_Object getParent() {
		return _parent;
	}
	
	public List<LA_Object> getChildren() {
		return _children;
	}

	static JDBC_Object findById(Long id) {
		return OBJECTS.get(id);
	}
	
	static JDBC_Object findByDescriptor(String descriptor) {
		for ( JDBC_Object object : OBJECTS.values() ) {
			if ( descriptor.equals(object.getDescriptor()) ) {
				return object;
			}
		}
		return null;
	}

}
