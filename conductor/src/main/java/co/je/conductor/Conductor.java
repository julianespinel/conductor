package co.je.conductor;

import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import co.je.conductor.infrastructure.config.ConductorConfig;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Conductor extends Application<ConductorConfig> {

	@Override
	public void initialize(Bootstrap<ConductorConfig> bootstrap) {

		bootstrap.addBundle(new Java8Bundle());
	}

	private void addCORSSupport(Environment environment) {

		Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,PATCH");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
	}

	@Override
	public void run(ConductorConfig conductorConfig, Environment environment) throws Exception {

		// add CORS support
		addCORSSupport(environment);

		MongoClient mongoClient = new MongoClient();
		DB mongoDB = mongoClient.getDB(conductorConfig.getMongoDBConfig().getDbName());
	}
	
	public static void main(String[] args) {
		
		try {
			
			Conductor conductor = new Conductor();
			conductor.run(args);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}