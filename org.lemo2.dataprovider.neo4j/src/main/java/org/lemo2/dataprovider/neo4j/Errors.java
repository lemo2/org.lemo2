package org.lemo2.dataprovider.neo4j;

public enum Errors {

	DATABASE_CONNECTION("Unable to connect to Neo4j database."),
	NULL_PARAMETER("Received Null reference as parameter for database query.");
	
	private final String description;
	
	private Errors(String description) {
		this.description = description;
	}
	
	public String concateMessages(String message) {
		return description + " - " + message;
	}
	
	@Override
	public String toString() {
		return this.description; 
	}
}
