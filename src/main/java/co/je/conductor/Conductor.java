package co.je.conductor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import co.je.conductor.api.resources.JobRequestResource;
import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.infrastructure.config.ConductorConfig;
import co.je.conductor.persistence.daos.JobRequestDAO;
import co.je.conductor.persistence.daos.JobResultDAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    public static ObjectMapper configureJackson(ObjectMapper objectMapper) {

        objectMapper.registerModule(new JodaModule());
        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    private JobRequestResource getJobRequestResource(DB mongoDB, ObjectMapper objectMapper) {

        JobRequestDAO jobRequestDAO = new JobRequestDAO(objectMapper);
        JobResultDAO jobResultDAO = new JobResultDAO(objectMapper);
        JobBusiness jobBusiness = new JobBusiness(mongoDB, jobRequestDAO, jobResultDAO);

        return new JobRequestResource(jobBusiness);
    }

    @Override
    public void run(ConductorConfig conductorConfig, Environment environment) throws Exception {

        // add CORS support
        addCORSSupport(environment);
        ObjectMapper objectMapper = environment.getObjectMapper();
        configureJackson(objectMapper);

        // MongoDB
        MongoClient mongoClient = new MongoClient();
        DB mongoDB = mongoClient.getDB(conductorConfig.getMongoDBConfig().getDbName());

        JobRequestResource jobRequestResource = getJobRequestResource(mongoDB, objectMapper);
        environment.jersey().register(jobRequestResource);
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