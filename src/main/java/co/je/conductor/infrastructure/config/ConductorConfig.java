package co.je.conductor.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ConductorConfig extends Configuration {
	
	private final MongoDBConfig mongoDBConfig;

	@JsonCreator
	public ConductorConfig(@JsonProperty("mongoDBConfig") MongoDBConfig mongoDBConfig) {
		
		this.mongoDBConfig = mongoDBConfig;
	}

	public MongoDBConfig getMongoDBConfig() {
		return mongoDBConfig;
	}
}