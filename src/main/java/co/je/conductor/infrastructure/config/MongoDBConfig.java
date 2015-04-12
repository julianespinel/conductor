package co.je.conductor.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MongoDBConfig {
	
	private final String dbName;

	@JsonCreator
	public MongoDBConfig(@JsonProperty("dbName") String dbName) {
		
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}
}