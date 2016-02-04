package org.lemo2.dataprovider.mongodb;

import java.util.HashMap;
import java.util.Map;

import org.lemo2.dataprovider.api.LA_Person;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Person;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDB_PersonDataProvider {

	private static Map<Integer, MongoDB_Person> INITIALIZED_PERSONS = new HashMap<Integer, MongoDB_Person>();
	
	public static void initializePerson(Integer personID, MongoDB_Person person) {
		INITIALIZED_PERSONS.put(personID, person);
	}
	
	public static LA_Person loadPersonByID(Integer personID) {	
		MongoDB_Person person = INITIALIZED_PERSONS.get(personID);
		
		// person is not yet initialized
		if (person == null) {
			DBCollection collection = MongoDB_Connector.connectToPersonCollection();
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", personID);
			
			DBCursor cursor = collection.find(whereQuery);
			while(cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				person = new MongoDB_Person(dbObj);
			}
		}
		
		return person;
	}
	
	public static LA_Person getPersonByDescriptor(String descriptor) {
		for (LA_Person person : INITIALIZED_PERSONS.values()) {
			if (person.equals(descriptor)) {
				return person;
			}
		}
		
		return null;
	}
}
