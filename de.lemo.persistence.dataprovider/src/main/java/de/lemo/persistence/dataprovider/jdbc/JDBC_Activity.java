package de.lemo.persistence.dataprovider.jdbc;

import de.lemo.persistence.dataprovider.*;

import java.sql.ResultSet;
import java.util.*;

public class JDBC_Activity implements LA_Activity {
	
	/**
	 * all instantiated learning activities, referenced by database ID
	 */
	static Map<Long,JDBC_Activity> ACTIVITY = new HashMap<Long,JDBC_Activity>();
		
	private LA_Person _person;
	private LA_Context _context;
	private LA_Object _object;
	private long _time;
	private String _action;
	private String _info;
	private LA_Activity _reference = null;	
	private Map<String,String> _extAttributes = new HashMap<String,String>();
	
	public JDBC_Activity(Long aid, Long cid, Long oid, Long pid,
			long time, String action, String info) {
		System.out.println("JDBC_Activity " + aid.longValue() + " " + cid.longValue() + " " + oid.longValue() + " "+ pid.longValue() + " " + time + " " + action + " " + info);
		ACTIVITY.put(aid, this);
		_context = JDBC_Context.findById(cid);
		_object = JDBC_Object.findById(oid);
		_person = JDBC_Person.findById(pid);
		_time = time;
		_action = action;
		_info = info;
	}

	public Set<String> extAttributes() {
		return _extAttributes.keySet();
	}
	
	public String getExtAttribute(String attr) {
		return _extAttributes.get(attr);
	}
	
	public LA_Person getPerson() {
		return _person;
	}
	
	public LA_Context getContext() {
		return _context;
	}
	
	public LA_Object getObject() {
		return _object;
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
	
	void setReference(LA_Activity reference) {
		_reference = reference;
	}
	
	static void initExtAttributes() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT attr,value,activity FROM D4LA_Activity_Ext");
		try {
			ResultSet rs = JDBC_DataProvider.executeQuery(new String(sb));
			while ( rs.next() ) {
				JDBC_Activity activity = findById(new Long(rs.getLong(3)));
				if ( activity != null ) {
					activity._extAttributes.put(rs.getString(1), rs.getString(2));
				}
			}
			rs.close();
		} catch ( Exception e ) { e.printStackTrace(); }
	}
	
	static void initReferences() {
		StringBuffer sb;
		ResultSet rs;
		try {
			sb = new StringBuffer();
			sb.append("SELECT id,reference from D4LA_Activity ");
			sb.append("WHERE reference IS NOT NULL");
			rs = JDBC_DataProvider.executeQuery(new String(sb));
			while ( rs.next() ) {
				JDBC_Activity activity = findById(new Long(rs.getLong(1)));
				if ( activity != null ) {
					activity._reference = findById(new Long(rs.getLong(2)));
				}
			}
			rs.close();
		} catch ( Exception e ) { e.printStackTrace(); }
	}
	
	static JDBC_Activity findById(Long pid) {
		return ACTIVITY.get(pid);
	}
	
}