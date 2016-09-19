package org.lemo2.dataprovider.neo4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	Neo4j_ActivityDataProviderTest.class,
	Neo4j_ContextDataProviderTest.class,
	Neo4j_PersonDataProviderTest.class
})

public class Neo4j_Lemo2TestSuite {

}
