package org.lemo2.dataprovider.mongodb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MongoDB_ActivityTest.class, MongoDB_ContextActivityTest.class })
public class DataProviderComparisonSuite {

}
