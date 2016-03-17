package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Object;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_PersonDataProvider {

	private static final String LOGIN_EXTENTION_ATTR = "login";
	
	private static Map<Integer, MongoDB_Person> INITIALIZED_PERSONS = new HashMap<Integer, MongoDB_Person>();
	
	public static void initializePerson(Integer personID, MongoDB_Person person) {
		INITIALIZED_PERSONS.put(personID, person);
	}
	
	/*
	 * FOR TEST
	 */
	public static void clearInitializedPersons() {
		INITIALIZED_PERSONS.clear();
	}
	
	/*
	 * FOR TEST
	 */
	public static int getSizeOfInitializedPersons() {
		return INITIALIZED_PERSONS.size();
	}
	
	public static MongoDB_Person getPersonByID(Integer personID) {	
		MongoDB_Person person = INITIALIZED_PERSONS.get(personID);
		
		// person is not yet initialized
		if (person == null) {
			DBCollection collection = MongoDB_Connector.connectToPersonCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", personID);
			
			DBCursor cursor = collection.find(whereQuery);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				person = createPersonObject(dbObj);
			}
		}
		
		return person;
	}
	
	/**
	 * Returns all persons with the IDs in the given list. 
	 * @param personIDs
	 * @return
	 */
	public static List<LA_Person> getPersonsByIDList(List<Integer> personIDs) {	
		List<LA_Person> persons = new ArrayList<LA_Person>();
		List<Integer> tmpIDs = new ArrayList<Integer>();
		
		MongoDB_Person person;
		
		// Load initialized persons
		for (int personID : personIDs) {
			person = INITIALIZED_PERSONS.get(personID); 
			if (person != null) {
				persons.add(person);
			}
			else {
				tmpIDs.add(personID);
			}
		}
		
		// load all persons which are not yet initialized
		DBCollection collection = MongoDB_Connector.connectToPersonCollection();
		
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new BasicDBObject("$in", tmpIDs));
		
		DBCursor cursor = collection.find(query);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			person = createPersonObject(dbObj);
			persons.add(person);
		}
		
		return persons;
	}
	
	/**
	 * Returns all learning activities for the given person
	 * @param personID
	 * @return
	 */
	public static List<LA_Activity> getPersonActivities(int personID) {
		List<LA_Activity> activities = new ArrayList<LA_Activity>();
		List<Integer> activityIDs = getPersonActivityIDs(personID);
		
		// load activities
		if (activityIDs != null && activityIDs.size() > 0) {
			activities = MongoDB_ActivityDataProvider.getActivitiesByIDList(activityIDs);
		}
		
		return activities;
	}
	
	/**
	 * Returns all Learning Activity IDs of the given Learning Context.
	 * @param contextID
	 * @return
	 */
	public static List<Integer> getPersonActivityIDs(int personID) {
		DBCollection collection = MongoDB_Connector.connectToPersonCollection();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("_id", personID);
		select.put("learningActivities", 1); // just return 'learningActivities' field
		
		DBObject result = collection.findOne(idQuery, select);
		
		List<Integer> activityIDs = (List<Integer>) result.get("learningActivities");
		
		return activityIDs;
	}
	
	/**
	 * Returns the person with the given login ID.
	 * It will first search if the person was already initialized.
	 * @param loginID
	 * @return
	 */
	public static MongoDB_Person getPersonByLoginID(String loginID) {
		// check if person with login id was already initialized
		for (MongoDB_Person person : INITIALIZED_PERSONS.values()) {
			if (person.getExtAttribute(LOGIN_EXTENTION_ATTR).equals(loginID)) {
				return person;
			}
		}
		
		MongoDB_Person person = getPersonWithLoginExtention(loginID);
		
		return person;
	}
	
	/**
	 * Return the person with 'login' extention and the given login id.
	 * @param loginID
	 * @return
	 */
	private static MongoDB_Person getPersonWithLoginExtention(String loginID) {
		MongoDB_Person person = null;
		
		DBCollection collection = MongoDB_Connector.connectToPersonCollection();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("extentions.ext_attr", LOGIN_EXTENTION_ATTR);
		idQuery.put("extentions.ext_value", loginID);
		select.put("_id", 1); // just return 'learningActivities' field
		
		DBCursor cursor = collection.find(idQuery, select);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			person = createPersonObject(dbObj);
		}
		
		return person;
	}
	
	public static List<MongoDB_Person> getAllPersonsWithLoginExtention() {
		List<MongoDB_Person> persons = new ArrayList<MongoDB_Person>();
		
		DBCollection collection = MongoDB_Connector.connectToPersonCollection();
		
		BasicDBObject idQuery = new BasicDBObject();
		BasicDBObject select = new BasicDBObject();
		
		idQuery.put("extentions.ext_attr", LOGIN_EXTENTION_ATTR);
		select.put("_id", 1); // just return 'learningActivities' field
		
		DBCursor cursor = collection.find(idQuery, select);
		while(cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			MongoDB_Person person = createPersonObject(dbObj);
			persons.add(person);
		}
		
		return persons;
	}
	
	public static MongoDB_Person getPersonByDescriptor(String descriptor) {
		for (MongoDB_Person person : INITIALIZED_PERSONS.values()) {
			if (person.getDescriptor().equals(descriptor)) {
				return person;
			}
		}
		
		return null;
	}
	
	protected static MongoDB_Person createPersonObject(DBObject dbObject) {
		int personID = (int) dbObject.get("_id");
		
		// load / create domain object
		MongoDB_Person person = INITIALIZED_PERSONS.get(personID);
		if (person == null) {
			person = new MongoDB_Person(dbObject);
			INITIALIZED_PERSONS.put(personID, person);
		}
		
		return person;
	}
	
}
