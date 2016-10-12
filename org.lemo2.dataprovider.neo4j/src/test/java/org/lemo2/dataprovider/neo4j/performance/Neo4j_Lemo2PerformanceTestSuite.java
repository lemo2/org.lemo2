package org.lemo2.dataprovider.neo4j.performance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Neo4j_ActivityDataProviderPerformanceTest.class,
		Neo4j_ContextDataProviderPerformanceTest.class,
		Neo4j_ObjectDataProviderPerformanceTest.class,
		Neo4j_PersonDataProviderPerformanceTest.class })
public class Neo4j_Lemo2PerformanceTestSuite {

}
