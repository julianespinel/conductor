package co.je.conductor.persistence.daos;

import org.bson.types.ObjectId;

import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.exceptions.TechnicalException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fj.data.Either;

public class JobRequestDAO {

    public static final String JOBS_REQUESTS_COLLECTION = "jobs_requests";

    private final ObjectMapper objectMapper;

    public JobRequestDAO(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    public Either<IException, String> createJobRequest(DB mongoDB, JobRequest jobRequest) {

        Either<IException, String> either = null;

        try {

            DBCollection collection = mongoDB.getCollection(JOBS_REQUESTS_COLLECTION);

            String jobRequestJson = objectMapper.writeValueAsString(jobRequest);
            DBObject jobRequestDBO = (DBObject) JSON.parse(jobRequestJson);

            ObjectId objectId = ObjectId.get();
            jobRequestDBO.put("_id", objectId);
            jobRequestDBO.put("id", objectId);
            collection.insert(jobRequestDBO);
            either = Either.right(objectId.toString());

        } catch (Exception e) {

            e.printStackTrace();
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);
        }

        return either;
    }
}