package co.je.conductor.persistence.daos;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.je.conductor.domain.entities.JobResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JobResultDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobResultDAO.class);

	public static final String JOBS_RESULTS_COLLECTION = "jobs_results";

	private final ObjectMapper objectMapper;

	public JobResultDAO(ObjectMapper objectMapper) {
		
		this.objectMapper = objectMapper;
	}

	public String saveJobResult(DB mongoDB, JobResult jobResult) {
		
		String jobResultId = "";

		try {
			
			DBCollection collection = mongoDB.getCollection(JOBS_RESULTS_COLLECTION);
			String jobResultJson = objectMapper.writeValueAsString(jobResult);
			
			DBObject jobResultDBO = (DBObject) JSON.parse(jobResultJson);

			ObjectId objectId = ObjectId.get();
			jobResultDBO.put("_id", objectId);
			jobResultDBO.put("id", objectId);
			collection.insert(jobResultDBO);

			jobResultId = objectId.toString();
			
		} catch (JsonProcessingException e) {
			
			LOGGER.error("saveJobResult", e);
			throw new IllegalArgumentException("The given json could not be parsed.");
		}
		
		return jobResultId;
	}
}