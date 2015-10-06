package org.lemo2.dataprovider.jdbc;

import java.util.*;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;



import com.mysql.jdbc.Driver;

import org.lemo2.dataprovider.api.*;

@Component
@Instantiate
@Provides
public class JDBC_DataProvider implements LA_DataProvider {
		
	private static final String STUDENT = "student";
	private static final String INSTRUCTOR = "instructor";
	private static final String HASH_USERID = "hash_userid";
	private static final String PLATFORM_ID = "platform_id";
	
	private boolean _debug;
	private PrintStream _ps;
	
	private Statement _statement = null;
	private String _uri;
	private String _user;
	private String _password;
	
	private long _studentRole;
	private long _instructorRole;
	private Map<Long,String> _actions;
	private Map<Long,String> _types;
	
	private List<String> _contextExt;
	private List<String> _personExt;
	private List<String> _objectExt;
	private List<String> _activityExt;
	
//	private static final Logger logger = LoggerFactory.getLogger(JDBC_DataProvider.class);
	
	public JDBC_DataProvider(String uri, String user, String password) {
		_uri = uri;
		_user = user;
		_password = password;
		initialize();
	}
	
	public JDBC_DataProvider(String uri, String user, String password, boolean debug, PrintStream ps) {
		_debug = debug;
		_ps = ps;
		_uri = uri;
		_user = user;
		_password = password;
		initialize();
	}
	
	private void initialize() {
		initRoles();
		initActions();
		initTypes();
		_contextExt = initExtAttributes("D4LA_Context_Ext");
		_personExt = initExtAttributes("D4LA_Person_Ext");
		_objectExt = initExtAttributes("D4LA_Object_Ext");
		_activityExt = initExtAttributes("D4LA_Activity_Ext");		
	}
	
	private List<String> initExtAttributes(String table) {
		List<String> attributes = new ArrayList<String>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT attr FROM ");
			sb.append(table);
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				attributes.add(rs.getString(1));
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		return attributes;
	}
	
	private void initActions() {
		_actions = new HashMap<Long,String>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT id,value FROM D4LA_Action");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				String value = rs.getString(2);
				_actions.put(id, value);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
	}

	private void initTypes() {
		_types = new HashMap<Long,String>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT id,value FROM D4LA_Type");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				String value = rs.getString(2);
				_types.put(id, value);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
	}

	private void initRoles() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT id,value FROM D4LA_Role");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				long id = rs.getLong(1);
				String value = rs.getString(2);
				if ( value.equals(STUDENT) ) _studentRole = id;
				else if ( value.equals(INSTRUCTOR) ) _instructorRole = id;
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
	}

	private ResultSet executeQuery(String sql) throws Exception {
		if ( _statement == null ) {
			Driver driver = new Driver();
			DriverManager.registerDriver(driver);
			Connection connection = DriverManager.getConnection(_uri, _user, _password);
			_statement = connection.createStatement();
		}
		ResultSet rs;
		long timing1 = 0;
		long timing2 = 0;
		if ( _debug ) {
			timing1 = System.currentTimeMillis();
		}
		rs = _statement.executeQuery(sql);
		if ( _debug ) {
			timing2 = System.currentTimeMillis();
			_ps.println(">> SQL " + sql + ": " + (timing2-timing1) + " ms");
		}
		return rs;
	}
	
	public List<LA_Context> getCourses() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,name FROM D4LA_Context WHERE parent IS NULL");
		return getCourses(new String(sb));
	}
	
	public List<LA_Context> getCoursesByInstructor(String hashUserId) {
		Long id = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT person FROM D4LA_Person_Ext ");
			sb.append("WHERE attr = '");
			sb.append(HASH_USERID);
			sb.append("' AND value = '");
			sb.append(hashUserId);
			sb.append("'");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				id = new Long(rs.getLong(1));
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		if ( id == null ) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT D4LA_Context.id,D4LA_Context.name FROM D4LA_Context ");
		sb.append("LEFT JOIN D4LA_Person_Context" );
		sb.append("ON D4LA_Context.id = D4LA_Person_Context.context ");
		sb.append("WHERE D4LA_Person_Context.person = ");
		sb.append(id.longValue());
		return getCourses(new String(sb));
	}
	
	public LA_Context getCourseByPlatformId(String platformId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT D4LA_Context.id,D4LA_Context.name FROM D4LA_Context ");
		sb.append("LEFT JOIN D4LA_Context_Ext ");
		sb.append("ON D4LA_Context.id = D4LA_Context_Ext.context ");
		sb.append("WHERE D4LA_Context_Ext.attr = '");
		sb.append(PLATFORM_ID);
		sb.append("' AND D4LA_Context_Ext.value = '");
		sb.append(platformId);
		sb.append("'");
		List<LA_Context> courses = getCourses(new String(sb));
		if ( courses == null ) return null;
		if ( courses.isEmpty() ) return null;
		return courses.get(0);
	}
	
	private List<LA_Context> getCourses(String sql) {
		List<LA_Context> courses;
		Set<Long> newContexts = new HashSet<Long>();
		courses = initCourses(sql, newContexts);
		initContextExt(newContexts);
		return courses;
	}
	
	public List<LA_Context> initCourses(String sql, Set<Long> newContexts) {
		List<LA_Context> courses = new ArrayList<LA_Context>();
		List<Long> parentContexts = new ArrayList<Long>();
		try {
			ResultSet rs = executeQuery(sql);
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				LA_Context context = JDBC_Context.findById(id);
				if ( context == null ) {
					String name = rs.getString(2);
					context = new JDBC_Context(id, name, this);
					newContexts.add(id);
					parentContexts.add(id);
				}
				courses.add(context);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		initChildContexts(parentContexts, newContexts);
		return courses;
	}
	
	private void initChildContexts(List<Long> parentContexts, Set<Long> newContexts) {
		while ( ! parentContexts.isEmpty() ) {
			List<Long> pcs = new ArrayList<Long>();
			for ( Long pid : parentContexts ) {
				try {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT id,name FROM D4LA_Context WHERE parent = ");
					sb.append(pid.longValue());
					ResultSet rs = executeQuery(new String(sb));
					while ( rs.next() ) {
						Long id = new Long(rs.getLong(1));
						JDBC_Context context = JDBC_Context.findById(id);
						if ( context == null ) {
							String name = rs.getString(2);
							context = new JDBC_Context(id, name, this);
							JDBC_Context parent = JDBC_Context.findById(pid);
							context._parent = parent;
							if ( parent._children == null) parent._children = new ArrayList<LA_Context>();
							parent._children.add(context);
							newContexts.add(id);
							pcs.add(id);
						}
					}
					rs.close();
				} catch ( Exception e ) {
					if ( _debug ) e.printStackTrace();
//					logger.error("Error during first access to courses", e);
				}			
			}
			parentContexts = pcs;
		}
	}
	
	List<LA_Person> initStudents(Long contextId) {
		return initPersons(contextId, _studentRole);
	}
	
	List<LA_Person> initInstructors(Long contextId) {
		return initPersons(contextId, _instructorRole);
	}
	
	private List<LA_Person> initPersons(Long contextId, long role) {
		List<LA_Person> persons = new ArrayList<LA_Person>();
		Set<Long> newPersons = new HashSet<Long>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT D4LA_Person.id,D4LA_Person.name FROM D4LA_Person ");
			sb.append("LEFT JOIN D4LA_Person_Context ");
			sb.append("ON D4LA_Person.id = D4LA_Person_Context.person ");
			sb.append("WHERE D4LA_Person_Context.role = ");
			sb.append(role);
			sb.append(" AND D4LA_Person_Context.context = ");
			sb.append(contextId.longValue());
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				String name = rs.getString(2);
				LA_Person person = JDBC_Person.findById(id);
				if ( person == null ) {
					person = new JDBC_Person(id, name);
					newPersons.add(id);
				}
				persons.add(person);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		if ( persons.isEmpty() ) return null;
		initPersonExt(newPersons);
		return persons;
	}
	
	List<LA_Object> initObjects(Long contextId) {
		List<LA_Object> objects = new ArrayList<LA_Object>();
		List<Long> parentObjects = new ArrayList<Long>();
		Set<Long> newObjects = new HashSet<Long>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT D4LA_Object.id,D4LA_Object.name,type FROM D4LA_Object ");
			sb.append("LEFT JOIN D4LA_Object_Context ");
			sb.append("ON D4LA_Object.id = D4LA_Object_Context.object ");
			sb.append("WHERE D4LA_Object_Context.context = ");
			sb.append(contextId.longValue());
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				String name = rs.getString(2);
				Long type = new Long(rs.getLong(3));
				LA_Object object = JDBC_Object.findById(id);
				if ( object == null ) {
					object = new JDBC_Object(id, name, _types.get(type));
					newObjects.add(id);
					parentObjects.add(id);
				}
				objects.add(object);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		if ( objects.isEmpty() ) return null;
		initChildObjects(parentObjects, newObjects);
		initObjectExt(newObjects);
		return objects;
	}
	
	private void initChildObjects(List<Long> parentObjects, Set<Long> newObjects) {
		while ( ! parentObjects.isEmpty() ) {
			List<Long> pcs = new ArrayList<Long>();
			for ( Long pid : parentObjects ) {
				try {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT id,name,type FROM D4LA_Object WHERE parent = ");
					sb.append(pid.longValue());
					ResultSet rs = executeQuery(new String(sb));
					while ( rs.next() ) {
						Long id = new Long(rs.getLong(1));
						JDBC_Object object = JDBC_Object.findById(id);
						if ( object == null ) {
							String name = rs.getString(2);
							String type = _types.get(new Long(rs.getLong(3)));
							object = new JDBC_Object(id, name, type);
							JDBC_Object parent = JDBC_Object.findById(pid);
							object._parent = parent;
							if ( parent._children == null) parent._children = new ArrayList<LA_Object>();
							parent._children.add(object);
							newObjects.add(id);
							pcs.add(id);
						}
					}
					rs.close();
				} catch ( Exception e ) {
					if ( _debug ) e.printStackTrace();
//					logger.error("Error during first access to courses", e);
				}			
			}
			parentObjects = pcs;
		}
	}
	
	List<LA_Activity> initActivities(Long contextId) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		Set<Long> newActivities = new HashSet<Long>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT id,time,action,info,reference,person,object FROM D4LA_Activity ");
			sb.append("WHERE context = ");
			sb.append(contextId.longValue());
			sb.append(" ORDER BY time");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				LA_Activity activity = JDBC_Activity.findById(id);
				if ( activity == null ) {
					long time = rs.getLong(2);
					long act = rs.getLong(3);
					String action = rs.wasNull() ? null : _actions.get(new Long(act));
					String info = rs.getString(4);
					long reference = rs.getLong(5);
					Long referenceId = rs.wasNull() ? null : new Long(reference);
					long person = rs.getLong(6);
					Long personId = rs.wasNull() ? null : new Long(person);
					long object = rs.getLong(7);
					Long objectId = rs.wasNull() ? null : new Long(object);
					activity = new JDBC_Activity(id, time, action, info,
							referenceId, contextId, personId, objectId);
					newActivities.add(id);
				}
				activities.add(activity);
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}
		initActivityExt(newActivities);
		if ( activities.isEmpty() ) return null;
		return activities;
	}
	
	private void initContextExt(Set<Long> newContexts) {
		if ( _contextExt == null ) return;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT context,attr,value FROM D4LA_Context_Ext");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				if ( newContexts.contains(id) ) {
					JDBC_Context context = JDBC_Context.findById(id);
					if ( context._extAttr == null ) {
						context._extAttr = new HashMap<String,String>();
					}
					context._extAttr.put(rs.getString(2), rs.getString(3));
				}
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}			
	}
	
	private void initPersonExt(Set<Long> newPersons) {
		if ( _personExt == null ) return;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT person,attr,value FROM D4LA_Person_Ext");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				if ( newPersons.contains(id) ) {
					JDBC_Person person = JDBC_Person.findById(id);
					if ( person._extAttr == null ) {
						person._extAttr = new HashMap<String,String>();
					}
					person._extAttr.put(rs.getString(2), rs.getString(3));
				}
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}			
	}
	
	private void initObjectExt(Set<Long> newObjects) {
		if ( _objectExt == null ) return;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT object,attr,value FROM D4LA_Object_Ext");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				if ( newObjects.contains(id) ) {
					JDBC_Object object = JDBC_Object.findById(id);
					if ( object._extAttr == null ) {
						object._extAttr = new HashMap<String,String>();
					}
					object._extAttr.put(rs.getString(2), rs.getString(3));
				}
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}			
	}
	
	private void initActivityExt(Set<Long> newActivities) {
		if ( _activityExt == null ) return;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT activity,attr,value FROM D4LA_Activity_Ext");
			ResultSet rs = executeQuery(new String(sb));
			while ( rs.next() ) {
				Long id = new Long(rs.getLong(1));
				if ( newActivities.contains(id) ) {
					JDBC_Activity activity = JDBC_Activity.findById(id);
					if ( activity._extAttr == null ) {
						activity._extAttr = new HashMap<String,String>();
					}
					activity._extAttr.put(rs.getString(2), rs.getString(3));
				}
			}
			rs.close();
		} catch ( Exception e ) {
			if ( _debug ) e.printStackTrace();
//			logger.error("Error during first access to courses", e);
		}			
	}
	
	public LA_Context getContext(String descriptor) {
		return JDBC_Context.findByDescriptor(descriptor);
	}
	
	public LA_Person getPerson(String descriptor) {
		return JDBC_Person.findByDescriptor(descriptor);
	}
	
	public LA_Object getObject(String descriptor) {
		return JDBC_Object.findByDescriptor(descriptor);
	}
	
}
