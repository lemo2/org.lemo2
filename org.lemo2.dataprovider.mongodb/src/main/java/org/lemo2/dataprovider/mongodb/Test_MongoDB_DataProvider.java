package org.lemo2.dataprovider.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.BSONObject;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Test_MongoDB_DataProvider {

	public static void main(String[] args) {
		testObjectExists();
	}
	
	private static void testObjectExists() {
		MongoClient mongoClient = mongoClient = new MongoClient( "localhost" , 27017 );
		DB mongoDatabase = mongoClient.getDB("iversity");
		
		DBCollection collection = mongoDatabase.getCollection("learningContext");
		
		DBObject query = new BasicDBObject("learningActivities", new BasicDBObject("$exists", true));
		DBObject result = collection.findOne(query);
		
		List<Integer> activityIDs = (List<Integer>) result.get("learningActivities");
	   
	    System.out.println(activityIDs);
	   
	}

}
