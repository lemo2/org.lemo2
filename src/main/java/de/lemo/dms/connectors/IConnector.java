/**
 * File ./main/java/de/lemo/dms/connectors/IConnector.java
 * Date 2013-01-24
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.dms.connectors;

/**
 * Interface for the connector classes
 *
 */
public interface IConnector {

	/**
	 * Tests the configured connection.
	 * 
	 * @param conf
	 *            the database configuration object, holding connection settings
	 * @return true, if successful
	 */
	boolean testConnections();

	/**
	 * Gets the data.
	 */
	void getData();

	/**
	 * Update data.
	 * 
	 * @param conf
	 *            the database configuration object, holding connection settings
	 * @param fromTimestamp
	 *            the from timestamp
	 */
	void updateData(long fromTimestamp);

	Long getPlatformId();

	Long getPrefix();

	String getName();

	ESourcePlatform getPlattformType();

}
