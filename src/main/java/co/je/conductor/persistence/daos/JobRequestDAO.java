package co.je.conductor.persistence.daos;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.je.conductor.domain.entities.JobRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JobRequestDAO {

	public static final String JOBS_REQUESTS_COLLECTION = "jobs_requests";

	private static final Logger LOGGER = LoggerFactory.getLogger(JobRequestDAO.class);

	private final ObjectMapper objectMapper;

	public JobRequestDAO(ObjectMapper objectMapper) {

		this.objectMapper = objectMapper;
	}

	public String createJobRequest(DB mongoDB, JobRequest jobRequest) {

		String jobRequestId = "";
		
		try {
			
			DBCollection collection = mongoDB.getCollection(JOBS_REQUESTS_COLLECTION);

			String jobRequestJson = objectMapper.writeValueAsString(jobRequest);
			DBObject jobRequestDBO = (DBObject) JSON.parse(jobRequestJson);

			ObjectId objectId = ObjectId.get();
			jobRequestDBO.put("_id", objectId);
			jobRequestDBO.put("id", objectId);
			collection.insert(jobRequestDBO);
			
			jobRequestId = objectId.toString();
			
		} catch (JsonProcessingException e) {
			
			LOGGER.error("createJobRequest", e);
			throw new IllegalArgumentException("Could not parse the given json");
		}

		return jobRequestId;
	}
}