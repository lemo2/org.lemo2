package org.lemo2.dataprovider.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDB_Connector {

	private static MongoClient mongoClient;
	private static DB mongoDatabase;
	
	private final static String COL_PERSON = "person";
	private final static String COL_LEARNING_ACTIVITY = "learningActivity";
	private final static String COL_LEARNING_CONTEXT = "learningContext";
	private final static String COL_LEARNING_OBJECT = "learningObject";
	
	public MongoDB_Connector(String host, String databaseName, int databasePort) {
		mongoClient = new MongoClient(host, databasePort);
        mongoDatabase = mongoClient.getDB(databaseName);
	}
	
	/**
	 * Creates a connection to the collection which holds the learning activity data.
	 * @return
	 */
	protected static DBCollection connectToActivityCollection() {
		return mongoDatabase.getCollection(COL_LEARNING_ACTIVITY);
	}

	/**
	 * Creates a connection to the collection which holds the learning context data.
	 * @return
	 */
	protected static DBCollection connectToContextCollection() {
		return mongoDatabase.getCollection(COL_LEARNING_CONTEXT);
	}
	
	/**
	 * Creates a connection to the collection which holds the learning object data.
	 * @return
	 */
	protected static DBCollection connectToObjectCollection() {
		return mongoDatabase.getCollection(COL_LEARNING_OBJECT);
	}
	
	/**
	 * Creates a connection to the collection which holds the persons data.
	 * @return
	 */
	protected static DBCollection connectToPersonCollection() {
		return mongoDatabase.getCollection(COL_PERSON);
	}
	
}
