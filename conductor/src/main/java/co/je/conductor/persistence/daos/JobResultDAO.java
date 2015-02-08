package co.je.conductor.persistence.daos;

import org.bson.types.ObjectId;

import co.je.conductor.domain.entities.JobResult;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.exceptions.TechnicalException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fj.data.Either;

public class JobResultDAO {

    public static final String JOBS_RESULTS_COLLECTION = "jobs_results";

    private final ObjectMapper objectMapper;

    public JobResultDAO(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    public Either<IException, String> saveJobResult(DB mongoDB, JobResult jobResult) {

        Either<IException, String> either = null;

        try {

            DBCollection collection = mongoDB.getCollection(JOBS_RESULTS_COLLECTION);
            
            String jobResultJson = objectMapper.writeValueAsString(jobResult);
            DBObject jobResultDBO = (DBObject) JSON.parse(jobResultJson);

            ObjectId objectId = ObjectId.get();
            jobResultDBO.put("_id", objectId);
            jobResultDBO.put("id", objectId);
            collection.insert(jobResultDBO);
            either = Either.right(objectId.toString());

        } catch (Exception e) {

            e.printStackTrace();
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);
        }

        return either;
    }
}
