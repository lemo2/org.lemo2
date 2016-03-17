package org.lemo2.dataprovider.mongodb;

public class ThreadTest {

	public static void main(String[] args) {
		String database = "iversity";
		String host = "localhost";
		int port = 27017;
		
		int contextID = 206;
		
		System.out.println("Create MongoDB-Conntection");
		MongoDB_DataProvider dataProvider = new MongoDB_DataProvider(host, database, port);
		
		ThreadDemo T1 = new ThreadDemo("CONTEXT", 206);
		ThreadDemo T2 = new ThreadDemo("ACTIVITY", 206);
		/*
		ThreadDemo T3 = new ThreadDemo("ACTIVITY", 206);
		ThreadDemo T4 = new ThreadDemo("ACTIVITY", 206);
		ThreadDemo T5 = new ThreadDemo("ACTIVITY", 206);
		*/
	    T1.start();
	    T2.start();
	    /*
	    T3.start();
	    T4.start();
	    T5.start();
		*/
	}

}
