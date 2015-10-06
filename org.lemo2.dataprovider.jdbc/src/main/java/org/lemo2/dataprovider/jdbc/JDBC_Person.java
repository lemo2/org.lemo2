package org.lemo2.dataprovider.jdbc;

import org.lemo2.dataprovider.api.*;

import java.util.*;

public class JDBC_Person implements LA_Person {
	
	/**
	 * all instantiated persons, referenced by database ID
	 */
	static Map<Long,JDBC_Person> PERSONS = new HashMap<Long,JDBC_Person>();
	
	String _name;
	String _descriptor;
	Map<String,String> _extAttr = null;
	
	public JDBC_Person(Long id, String name) {
		_name = name;
		_descriptor = Integer.toString(hashCode());
		PERSONS.put(id, this);
	}

	public String getName() {
		return _name;
	}
	
	public String getDescriptor() {
		return _descriptor;
	}
	
	public Set<String> getExtNames() {
		if ( _extAttr == null ) return null;
		return _extAttr.keySet();
	}
	
	public String getExtValue(String name) {
		if ( _extAttr == null ) return null;
		return _extAttr.get(name);
	}
	
	static JDBC_Person findById(Long id) {
		return PERSONS.get(id);
	}
	
	static JDBC_Person findByDescriptor(String descriptor) {
		for ( JDBC_Person person : PERSONS.values() ) {
			if ( descriptor.equals(person.getDescriptor()) ) {
				return person;
			}
		}
		return null;
	}
	
	
}
