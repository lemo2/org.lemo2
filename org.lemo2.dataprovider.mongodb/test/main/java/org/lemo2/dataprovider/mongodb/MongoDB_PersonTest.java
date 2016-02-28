package org.lemo2.dataprovider.mongodb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class MongoDB_PersonTest {

	private static MongoDB_DataProvider dataProvider;
	
	@BeforeClass
	public static void setUp() throws Exception {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		System.out.println("Create MongoDB-Conntection to person collection");
		dataProvider = new MongoDB_DataProvider(host, database, port);
	}

}
