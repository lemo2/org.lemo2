package org.lemo2.dataprovider.neo4j;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Neo4j_Lemo2TestRunner {

	public static void main(String[] args) {
		Neo4j_Lemo2TestDataCreator dataCreator = new Neo4j_Lemo2TestDataCreator();
		//dataCreator.createTestdata();
		
		Result result = JUnitCore.runClasses(Neo4j_Lemo2TestSuite.class);

	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      if (result.wasSuccessful()) {
	    	  System.out.println("Testsuite was successful.");
	      }
	      else {
	    	  System.out.println("Testsuite completed with errors.");
	      }

	}

}
